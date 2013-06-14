// Copyright 2007-2012 Chris Patterson, Dru Sellers, Travis Smith, et. al.
//  
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this file except in compliance with the License. You may obtain a copy of the 
// License at 
// 
//     http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
// CONDITIONS OF ANY KIND, either express or implied. See the License for the 
// specific language governing permissions and limitations under the License.
package com.masstransitproject.crosstown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.masstransitproject.crosstown.pipeline.IOutboundMessagePipeline;

    /// <summary>
    /// A service bus is used to attach message handlers (services) to endpoints, as well as
    /// communicate with other service bus instances in a distributed application
    /// </summary>
    public class ServiceBus implements
        IControlBus
    {
        private static final Logger  _log = LoggerFactory.getLogger(ServiceBus.class);

        //ConsumerPool _consumerPool;
        //int _consumerThreadLimit = Environment.ProcessorCount*4;
        //ServiceBusInstancePerformanceCounters _counters;
        //volatile bool _disposed;
        //UntypedChannel _eventChannel;
        //ChannelConnection _performanceCounterConnection;
        //int _receiveThreadLimit = 1;
        //TimeSpan _receiveTimeout = 3.Seconds();
        //IServiceContainer _serviceContainer;
        //volatile boolean _started;

        

        /// <summary>
        /// Creates an instance of the ServiceBus, which implements IServiceBus. This is normally
        /// not called and should be created using the ServiceBusConfigurator to ensure proper defaults
        /// and operation.
        /// </summary>
        public ServiceBus(IEndpoint endpointToListenOn,
            IEndpointCache endpointCache)
        {
            ReceiveTimeout = TimeSpan.FromSeconds(3);
            Guard.AgainstNull(endpointToListenOn, "endpointToListenOn", "This parameter cannot be null");
            Guard.AgainstNull(endpointCache, "endpointFactory", "This parameter cannot be null");

            Endpoint = endpointToListenOn;
            EndpointCache = endpointCache;

            _eventChannel = new ChannelAdapter();

            _serviceContainer = new ServiceContainer(this);

            OutboundPipeline = new OutboundPipelineConfigurator(this).Pipeline;
            InboundPipeline = InboundPipelineConfigurator.CreateDefault(this);

            ControlBus = this;

            InitializePerformanceCounters();
        }

//        public int ConcurrentReceiveThreads
//        {
//            get { return _receiveThreadLimit; }
//            set
//            {
//                if (_started)
//                    throw new ConfigurationException(
//                        "The receive thread limit cannot be changed once the bus is in motion. Beep! Beep!");
//
//                _receiveThreadLimit = value;
//            }
//        }
//
//        public int MaximumConsumerThreads
//        {
//            get { return _consumerThreadLimit; }
//            set
//            {
//                if (_started)
//                    throw new ConfigurationException(
//                        "The consumer thread limit cannot be changed once the bus is in motion. Beep! Beep!");
//
//                _consumerThreadLimit = value;
//            }
//        }
//
//        public TimeSpan ReceiveTimeout
//        {
//            get { return _receiveTimeout; }
//            set
//            {
//                if (_started)
//                    throw new ConfigurationException(
//                        "The receive timeout cannot be changed once the bus is in motion. Beep! Beep!");
//
//                _receiveTimeout = value;
//            }
//        }
//
//        public TimeSpan ShutdownTimeout { get; set; }
//
//        public UntypedChannel EventChannel
//        {
//            get { return _eventChannel; }
//        }
//
//        [UsedImplicitly]
//        protected String DebugDisplay
//        {
//            get { return String.Format("{0}: ", Endpoint.Address); }
//        }

        private IEndpointCache endpointCache;

        public IEndpointCache getEndpointCache() {
			return endpointCache;
		}

		public void setEndpointCache(IEndpointCache endpointCache) {
			this.endpointCache = endpointCache;
		}

		public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        public void Publish(T message)
        {
            Publish(message, NoContext);
        }

        /// <summary>
        /// Publishes a message to all subscribed consumers for the message type
        /// </summary>
        /// <typeparam name="T">The type of the message</typeparam>
        /// <param name="message">The messages to be published</param>
        /// <param name="contextCallback">The callback to perform operations on the context</param>
        public void Publish(T message, SendCallback<T> contextCallback)
        {
            PublishContext<T> context = ContextStorage.CreatePublishContext(message);
            context.SetSourceAddress(Endpoint.Address.Uri);

            contextCallback(context);

            IList<Exception> exceptions = new List<Exception>();

            int publishedCount = 0;
            foreach (object consumer in OutboundPipeline.Enumerate(context))
            {
                try
                {
                    consumer(context);
                    publishedCount++;
                }
                catch (Exception ex)
                {
                    _log.Error(String.Format("'{0}' threw an exception publishing message '{1}'",
                        consumer.GetType().FullName, message.GetType().FullName), ex);

                    exceptions.Add(ex);
                }
            }

            context.Complete();

            if (publishedCount == 0)
            {
                context.NotifyNoSubscribers();
            }

            _eventChannel.Send(new MessagePublished
                {
                    MessageType = typeof(T),
                    ConsumerCount = publishedCount,
                    Duration = context.Duration,
                });

            if (exceptions.Count > 0)
                throw new PublishException(typeof(T), exceptions);
        }

        public void Publish(Object message)
        {
            if (message == null)
                throw new IllegalArgumentException("Null message");

            BusObjectPublisherCache.Instance[message.GetType()].Publish(this, message);
        }

        public void Publish(Object message, Type messageType)
        {
            if (message == null)
                throw new IllegalArgumentException("Null message");
            if (messageType == null)
                throw new IllegalArgumentException("Null messageType");

            BusObjectPublisherCache.Instance[messageType].Publish(this, message);
        }

        public void Publish(Object message, SendCallback contextCallback)
        {
            if (message == null)
                throw new IllegalArgumentException("Null message");
            if (contextCallback == null)
                throw new IllegalArgumentException("Null contextCallback");

            BusObjectPublisherCache.Instance[message.GetType()].Publish(this, message, contextCallback);
        }

        public void Publish(Object message, Type messageType, SendCallback<T> contextCallback)
        {
            if (message == null)
                throw new IllegalArgumentException("Null message");
            if (messageType == null)
                throw new IllegalArgumentException("Null messageType");
            if (contextCallback == null)
                throw new IllegalArgumentException("Null contextCallback");

            BusObjectPublisherCache.Instance[messageType].Publish(this, message, contextCallback);
        }

        /// <summary>
        /// <see cref="IServiceBus.Publish{T}"/>: this is a "dynamically"
        /// typed overload - give it an interface as its type parameter,
        /// and a loosely typed dictionary of values and the MassTransit
        /// underlying infrastructure will populate an Object instance
        /// with the passed values. It actually does this with DynamicProxy
        /// in the background.
        /// </summary>
        /// <typeparam name="T">The type of the interface or
        /// non-sealed class with all-virtual members.</typeparam>
        /// <param name="bus">The bus to publish on.</param>
        /// <param name="values">The dictionary of values to place in the
        /// Object instance to implement the interface.</param>
        public void Publish<T>(Object values)
            where T : class
        {
            if (values == null)
                throw new IllegalArgumentException("Null values");

            var message = InterfaceImplementationExtensions.InitializeProxy<T>(values);

            Publish(message, x => { });
        }

        /// <summary>
        /// <see cref="Publish{T}(MassTransit.IServiceBus,Object)"/>: this
        /// overload further takes an action; it allows you to set <see cref="IPublishContext"/>
        /// meta-data. Also <see cref="IServiceBus.Publish{T}"/>.
        /// </summary>
        /// <typeparam name="T">The type of the message to publish</typeparam>
        /// <param name="bus">The bus to publish the message on.</param>
        /// <param name="values">The dictionary of values to become hydrated and
        /// published under the type of the interface.</param>
        /// <param name="contextCallback">The context callback.</param>
        public void Publish<T>(Object values, Action<IPublishContext<T>> contextCallback)
        {
            if (values == null)
                throw new IllegalArgumentException("Null values");

            var message = InterfaceImplementationExtensions.InitializeProxy<T>(values);

            Publish(message, contextCallback);
        }

        public IOutboundMessagePipeline OutboundPipeline;

//        public IInboundMessagePipeline InboundPipeline { get; private set; }

        /// <summary>
        /// The endpoint associated with this instance
        /// </summary>
        private IEndpoint Endpoint;

        public IEndpoint getEndpoint() {
			return Endpoint;
		}

		public void setEndpoint(IEndpoint endpoint) {
			Endpoint = endpoint;
		}

		public UnsubscribeAction Configure(Func<IInboundPipelineConfigurator, UnsubscribeAction> configure)
        {
            return InboundPipeline.Configure(configure);
        }

        public IServiceBus ControlBus;

        public IServiceBus getControlBus() {
			return ControlBus;
		}

		public void setControlBus(IServiceBus controlBus) {
			ControlBus = controlBus;
		}

		public IEndpoint GetEndpoint(URI address)
        {
            return EndpointCache.GetEndpoint(address);
        }

        public void Inspect(DiagnosticsProbe probe)
        {
            new StandardDiagnosticsInfo().WriteCommonItems(probe);

            probe.Add("mt.version", typeof(IServiceBus).Assembly.GetName().Version);
            probe.Add("mt.receive_from", Endpoint.Address);
            probe.Add("mt.control_bus", ControlBus.Endpoint.Address);
            probe.Add("mt.max_consumer_threads", MaximumConsumerThreads);
            probe.Add("mt.concurrent_receive_threads", ConcurrentReceiveThreads);
            probe.Add("mt.receive_timeout", ReceiveTimeout);

            EndpointCache.Inspect(probe);
            _serviceContainer.Inspect(probe);

            OutboundPipeline.View(pipe => probe.Add("zz.mt.outbound_pipeline", pipe));
            InboundPipeline.View(pipe => probe.Add("zz.mt.inbound_pipeline", pipe));
        }
//
//        public IBusService GetService(Class type)
//        {
//            return _serviceContainer.GetService(type);
//        }

        public bool TryGetService(Class type, out IBusService result)
        {
            return _serviceContainer.TryGetService(type, out result);
        }

        void NoContext<T>(IPublishContext<T> context)
            where T : class
        {
        }

        public void Start()
        {
            if (_started)
                return;

            try
            {
                _serviceContainer.Start();

                _consumerPool = new ThreadPoolConsumerPool(this, _eventChannel, _receiveTimeout)
                    {
                        MaximumConsumerCount = MaximumConsumerThreads,
                    };
                _consumerPool.Start();
            }
            catch (Exception)
            {
                if (_consumerPool != null)
                    _consumerPool.Dispose();

                throw;
            }

            _started = true;
        }

        public void AddService(BusServiceLayer layer, IBusService service)
        {
            _serviceContainer.AddService(layer, service);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (_disposed)
                return;
            if (disposing)
            {
                if (_consumerPool != null)
                {
                    _consumerPool.Stop();
                    _consumerPool.Dispose();
                    _consumerPool = null;
                }

                if (_serviceContainer != null)
                {
                    _serviceContainer.Stop();
                    _serviceContainer.Dispose();
                    _serviceContainer = null;
                }

                if (ControlBus != this)
                    ControlBus.Dispose();

                if (_performanceCounterConnection != null)
                {
                    _performanceCounterConnection.Dispose();
                    _performanceCounterConnection = null;
                }

                _eventChannel = null;

                Endpoint = null;

                if (_counters != null)
                {
                    _counters.Dispose();
                    _counters = null;
                }

                EndpointCache.Dispose();
            }
            _disposed = true;
        }

        void InitializePerformanceCounters()
        {
            try
            {
                String instanceName = String.Format("{0}_{1}{2}",
                    Endpoint.Address.Uri.Scheme, Endpoint.Address.Uri.Host, Endpoint.Address.Uri.AbsolutePath.Replace("/", "_"));

                _counters = new ServiceBusInstancePerformanceCounters(instanceName);

                _performanceCounterConnection = _eventChannel.Connect(x =>
                    {
                        x.AddConsumerOf<MessageReceived>()
                            .UsingConsumer(message =>
                                {
                                    _counters.ReceiveCount.Increment();
                                    _counters.ReceiveRate.Increment();
                                    _counters.ReceiveDuration.IncrementBy(
                                        (long)message.ReceiveDuration.TotalMilliseconds);
                                    _counters.ReceiveDurationBase.Increment();
                                    _counters.ConsumerDuration.IncrementBy(
                                        (long)message.ConsumeDuration.TotalMilliseconds);
                                    _counters.ConsumerDurationBase.Increment();
                                });

                        x.AddConsumerOf<MessagePublished>()
                            .UsingConsumer(message =>
                                {
                                    _counters.PublishCount.Increment();
                                    _counters.PublishRate.Increment();
                                    _counters.PublishDuration.IncrementBy((long)message.Duration.TotalMilliseconds);
                                    _counters.PublishDurationBase.Increment();

                                    _counters.SentCount.IncrementBy(message.ConsumerCount);
                                    _counters.SendRate.IncrementBy(message.ConsumerCount);
                                });

                        x.AddConsumerOf<ThreadPoolEvent>()
                            .UsingConsumer(message =>
                                {
                                    _counters.ReceiveThreadCount.Set(message.ReceiverCount);
                                    _counters.ConsumerThreadCount.Set(message.ConsumerCount);
                                });
                    });
            }
            catch (Exception ex)
            {
                _log.Warn(
                    "The performance counters could not be created, try running the program in the Administrator role. Just once.",
                    ex);
            }
        }
    }
}