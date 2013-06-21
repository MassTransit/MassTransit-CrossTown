package com.masstransitproject.crosstown.pipeline;

import com.masstransitproject.crosstown.IServiceBus;
import com.masstransitproject.crosstown.handlers.UnregisterAction;

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

	/// <summary>
	/// <para>Implementors should configure the pipeline managing subscriptions 
	/// to messages; both correlated by and by-type subscriptions.</para>
	/// 
	/// See also <see cref="ISubscriptionEvent"/> - which is passed to implementers of this
	/// interface.
	/// </summary>
	public interface IInboundPipelineConfigurator extends
		ISubscriptionEvent
	{
		/// <summary>
		/// Gets the inbound message pipeline.
		/// </summary>
		IInboundMessagePipeline getPipeline ();
		
		/// <summary>
		/// Gets the service bus under configuration.
		/// </summary>
		IServiceBus getBus();

		/// <summary>
		/// Register some instance that cares about message-subscriptions.
		/// </summary>
		/// <param name="subscriptionEventHandler">Instance</param>
		/// <returns>An unsubscribing multi-cast delegate.</returns>
		void Register(ISubscriptionEvent subscriptionEventHandler);
		void Unregister(ISubscriptionEvent subscriptionEventHandler);
	}
