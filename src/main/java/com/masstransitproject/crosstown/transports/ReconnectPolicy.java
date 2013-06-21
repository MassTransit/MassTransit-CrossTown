package com.masstransitproject.crosstown.transports;

import com.masstransitproject.crosstown.handlers.ConnectionPolicyCallback;

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

	

	public class ReconnectPolicy implements
		ConnectionPolicy
	{
		final ConnectionHandler _connectionHandler;
		final ConnectionPolicyChain _policyChain;
		final long _reconnectDelay;

		public ReconnectPolicy(ConnectionHandler connectionHandler, ConnectionPolicyChain policyChain, long reconnectDelay)
		{
			_connectionHandler = connectionHandler;
			_policyChain = policyChain;
			_reconnectDelay = reconnectDelay;
		}

		public void Execute(ConnectionPolicyCallback callback)
		{
			_connectionHandler.Disconnect();

			if (_reconnectDelay > 0)
				try {
					Thread.sleep(_reconnectDelay);
				} catch (InterruptedException e) {
					//Ignore
				}

			_connectionHandler.Connect();

			_policyChain.Pop(this);
			_policyChain.Next(callback);
		}
	}
