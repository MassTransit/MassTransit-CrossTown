package com.masstransitproject.crosstown.transports;

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
    /// Used to format a message type into a MessageName, which can be used as a valid
    /// queue name on the transport
    /// </summary>
    public interface IMessageNameFormatter
    {
        MessageName getMessageName(Class type);
    }
