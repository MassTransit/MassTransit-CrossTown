package com.masstransitproject.crosstown;

import java.net.URI;

import com.masstransitproject.crosstown.handlers.ConfigureCallback;
import com.masstransitproject.crosstown.handlers.SendCallback;
import com.masstransitproject.crosstown.handlers.UnsubscribeAction;
import com.masstransitproject.crosstown.handlers.UnsubscribeHandler;
import com.masstransitproject.crosstown.pipeline.IOutboundMessagePipeline;

// Copyright 2007-2011 Chris Patterson, Dru Sellers, Travis Smith, et. al.
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

//
//    /// <summary>
//    ///   The action to call to unsubscribe a previously subscribed consumer.
//    /// </summary>
//    /// <returns></returns>
//    public delegate bool UnsubscribeAction();
//
//    /// <summary>
//    ///   The action to call to unregister a previously registered component
//    /// </summary>
//    /// <returns></returns>
//    public delegate bool UnregisterAction();

    /// <summary>
    ///   The base service bus interface
    /// </summary>
    public interface IServiceBus 
    {
        /// <summary>
        ///   The endpoint from which messages are received
        /// </summary>
        IEndpoint getEndpoint();

        /// <summary>
        /// Gets the inbound message pipeline.
        /// </summary>
        //IInboundMessagePipeline InboundPipeline { get; }

        /// <summary>
        /// Gets the outbound message pipeline.
        /// </summary>
        IOutboundMessagePipeline getOutboundPipeline();

        /// <summary>
        /// Gets the control bus that can be used 
        /// to add/remove subscripts, move message 
        /// handlers around and tap runtime metrics
        /// from the service bus.
        /// </summary>
        IServiceBus getControlBus();

        /// <summary>
        /// Gets the endpoint cache. This property is used
        /// by <see cref="GetEndpoint"/> method in turn.
        /// </summary>
        IEndpointCache getEndpointCache();

        /// <summary>
        /// Gets or Sets the timeout used to wait for consumers to finish when shutting the bus down.
        /// </summary>
        long getShutdownTimeout();

        /// <summary>
        /// <para>Publishes a message to all subscribed consumers for the message type as specified
        /// by the generic parameter. The second parameter allows the caller to customize the
        /// outgoing publish context and set things like headers on the message.</para>
        /// 
        /// <para>
        /// Read up on publishing: http://readthedocs.org/docs/masstransit/en/latest/overview/publishing.html
        /// </para>
        /// </summary>
        /// <typeparam name = "T">The type of the message</typeparam>
        /// <param name = "message">The messages to be published</param>
        <T> void Publish(T message);

        /// <summary>
        /// <para>Publishes a message to all subscribed consumers for the message type as specified
        /// by the generic parameter. The second parameter allows the caller to customize the
        /// outgoing publish context and set things like headers on the message.</para>
        /// 
        /// <para>
        /// Read up on publishing: http://readthedocs.org/docs/masstransit/en/latest/overview/publishing.html
        /// </para>
        /// </summary>
        /// <typeparam name = "T">The type of the message</typeparam>
        /// <param name = "message">The messages to be published</param>
        /// <param name = "contextCallback">A callback that gives the caller
        /// access to the publish context.</param>
         <T> void Publish(T message, SendCallback contextCallback);

//        /// <summary>
//        /// Publishes an Object as a message, using the message type specified. If the Object cannot be cast
//        /// to the specified message type, an exception will be thrown.
//        /// </summary>
//        /// <param name="message">The message Object</param>
//        void Publish(Object message);

//        /// <summary>
//        /// Publishes an Object as a message, using the message type specified. If the Object cannot be cast
//        /// to the specified message type, an exception will be thrown.
//        /// </summary>
//        /// <param name="message">The message Object</param>
//        /// <param name="messageType">The type of the message (use message.getClass() if desired)</param>
//        void Publish(Object message, Class messageType);

//        /// <summary>
//        /// Publishes an Object as a message, using the message type specified. If the Object cannot be cast
//        /// to the specified message type, an exception will be thrown.
//        /// </summary>
//        /// <param name="message">The message Object</param>
//        /// <param name = "contextCallback">A callback that gives the caller
//        /// access to the publish context.</param>
//        void Publish(Object message, SendCallback contextCallback);

//        /// <summary>
//        /// Publishes an Object as a message, using the message type specified. If the Object cannot be cast
//        /// to the specified message type, an exception will be thrown.
//        /// </summary>
//        /// <param name="message">The message Object</param>
//        /// <param name="messageType">The type of the message (use message.getClass() if desired)</param>
//        /// <param name = "contextCallback">A callback that gives the caller
//        /// access to the publish context.</param>
//        void Publish(Object message, Class messageType, SendCallback contextCallback);
        
//        /// <summary>
//        /// <see cref="IServiceBus.Publish{T}"/>: this is a "dynamically"
//        /// typed overload - give it an interface as its type parameter,
//        /// and a loosely typed dictionary of values and the MassTransit
//        /// underlying infrastructure will populate an Object instance
//        /// with the passed values. It actually does this with DynamicProxy
//        /// in the background.
//        /// </summary>
//        /// <typeparam name="T">The type of the interface or
//        /// non-sealed class with all-virtual members.</typeparam>
//        /// <param name="bus">The bus to publish on.</param>
//        /// <param name="values">The dictionary of values to place in the
//        /// Object instance to implement the interface.</param>
//        void Publish(Object values);

//        /// <summary>
//        /// <see cref="Publish{T}(MassTransit.IServiceBus,Object)"/>: this
//        /// overload further takes an action; it allows you to set <see cref="IPublishContext"/>
//        /// meta-data. Also <see cref="IServiceBus.Publish{T}"/>.
//        /// </summary>
//        /// <typeparam name="T">The type of the message to publish</typeparam>
//        /// <param name="bus">The bus to publish the message on.</param>
//        /// <param name="values">The dictionary of values to become hydrated and
//        /// published under the type of the interface.</param>
//        /// <param name="contextCallback">The context callback.</param>
//        void Publish(Object values, SendCallback contextCallback);

        /// <summary>
        /// Looks an endpoint up by its uri.
        /// </summary>
        /// <param name="address"></param>
        /// <returns>The endpoint that corresponds to the uri passed</returns>
        IEndpoint GetEndpoint(URI  address);

        /// <summary>
        ///   Not sure this is going to make it, but trying a new approach.
        /// </summary>
        /// <param name = "configure"></param>
        /// <returns>An unsubscribe action that can be called to unsubscribe
        /// what was configured to be subscribed with the func passed. <see cref="UnsubscribeAction"/>.</returns>
        ConfigureCallback Configure(ConfigureCallback configure);

        /// <summary>
        /// Get the first service with the matching type, throwing an InvalidOperationException if none is found.
        /// </summary>
        /// <param name="type">The type of service to get.</param>
        /// <returns>The first service of type T.</returns>
        IBusService GetService(Class type);

	    /// <summary>
	    /// Try to get the first service with the matching type.
	    /// </summary>
	    /// <param name="type">The type of service to get.</param>
	    /// <param name="result">The service.</param>
	    /// <returns>Whether the service was found.</returns>
        IBusService getService(Class type);
        
        void Dispose();
    
}