package com.masstransitproject.crosstown.messages;

import java.io.Serializable;
import java.util.UUID;

import com.masstransitproject.crosstown.ExternallyNamespaced;

// Copyright 2007-2008 The Apache Software Foundation.
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

@SuppressWarnings("serial")
public class ResponseMessage implements Serializable, ExternallyNamespaced {
	private final UUID _correlationId;

	// xml serializer
	public ResponseMessage() {
		_correlationId = null;
	}

	public ResponseMessage(UUID correlationId) {
		_correlationId = correlationId;
	}

	public UUID getCorrelationId() {
		return _correlationId;
	}

	@Override
	public String getExternalNamespace() {
		return "MassTransit.TestFramework.Examples.Messages";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_correlationId == null) ? 0 : _correlationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseMessage other = (ResponseMessage) obj;
		if (_correlationId == null) {
			if (other._correlationId != null)
				return false;
		} else if (!_correlationId.equals(other._correlationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResponseMessage [_correlationId=" + _correlationId + "]";
	}

}