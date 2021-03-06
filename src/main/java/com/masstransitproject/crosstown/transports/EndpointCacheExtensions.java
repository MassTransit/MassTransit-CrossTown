package com.masstransitproject.crosstown.transports;

import java.net.URI;
import java.net.URISyntaxException;

import com.masstransitproject.crosstown.IEndpoint;
import com.masstransitproject.crosstown.IEndpointCache;

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

	public  class EndpointCacheExtensions
	{
		/// <summary>
		/// Returns an IEndpoint for the Uri string specified. If the endpoint has not yet been created,
		/// the factory will attempt to create an endpoint for the Uri string.
		/// </summary>
		/// <param name="cache"></param>
		/// <param name="uriString">The Uri string to resolve to an endpoint (will be checked for valid Uri syntax)</param>
		/// <returns>An IEndpoint instance</returns>
		public static IEndpoint GetEndpoint( IEndpointCache cache, String uriString)
		{
			URI uri;
			try {
				uri = new URI(uriString);
			} catch (URISyntaxException e) {
				throw new RuntimeException("The endpoint URI was invalid",e);
			}
			return cache.GetEndpoint(uri);//uriString.ToUri("The endpoint URI was invalid"));
		}
	}
