package com.masstransitproject.crosstown.context;

import java.net.URI;
import java.util.Date;

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


    public abstract class MessageContext implements IMessageContext 
    {
        IMessageHeaders _headers;

        public MessageContext()
        {
            _headers = new MessageHeaders();
        }

        private String MessageId;
        private String MessageType;
        private String ContentType;
        private String RequestId;
        private String ConversationId;
        private String CorrelationId;
        private URI SourceAddress;
        private URI InputAddress;
        private URI DestinationAddress;
        private URI ResponseAddress;
        private URI FaultAddress;
        private String Network;
        private Date ExpirationTime;
        private int RetryCount;

        private String OriginalMessageId;

       
        public String getOriginalMessageId() {
			return OriginalMessageId;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getHeaders()
		 */
        @Override
		public IMessageHeaders getHeaders()
        {
            return _headers; 
        }


		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getMessageId()
		 */
		@Override
		public String getMessageId() {
			return MessageId;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getMessageType()
		 */
		@Override
		public String getMessageType() {
			return MessageType;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getContentType()
		 */
		@Override
		public String getContentType() {
			return ContentType;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getRequestId()
		 */
		@Override
		public String getRequestId() {
			return RequestId;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getConversationId()
		 */
		@Override
		public String getConversationId() {
			return ConversationId;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getCorrelationId()
		 */
		@Override
		public String getCorrelationId() {
			return CorrelationId;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getSourceAddress()
		 */
		@Override
		public URI getSourceAddress() {
			return SourceAddress;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getInputAddress()
		 */
		@Override
		public URI getInputAddress() {
			return InputAddress;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getDestinationAddress()
		 */
		@Override
		public URI getDestinationAddress() {
			return DestinationAddress;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getResponseAddress()
		 */
		@Override
		public URI getResponseAddress() {
			return ResponseAddress;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getFaultAddress()
		 */
		@Override
		public URI getFaultAddress() {
			return FaultAddress;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getNetwork()
		 */
		@Override
		public String getNetwork() {
			return Network;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getExpirationTime()
		 */
		@Override
		public Date getExpirationTime() {
			return ExpirationTime;
		}

		/* (non-Javadoc)
		 * @see com.masstransitproject.crosstown.context.IMessageContext#getRetryCount()
		 */
		@Override
		public int getRetryCount() {
			return RetryCount;
		}


		public void setMessageId(String messageId) {
			MessageId = messageId;
		}

		public void setMessageType(String messageType) {
			MessageType = messageType;
		}

		public void setContentType(String contentType) {
			ContentType = contentType;
		}

		public void setRequestId(String requestId) {
			RequestId = requestId;
		}

		public void setConversationId(String conversationId) {
			ConversationId = conversationId;
		}

		public void setCorrelationId(String correlationId) {
			CorrelationId = correlationId;
		}

		public void setSourceAddress(URI sourceAddress) {
			SourceAddress = sourceAddress;
		}

		public void setInputAddress(URI inputAddress) {
			InputAddress = inputAddress;
		}

		public void setDestinationAddress(URI destinationAddress) {
			DestinationAddress = destinationAddress;
		}

		public void setResponseAddress(URI responseAddress) {
			ResponseAddress = responseAddress;
		}

		public void setFaultAddress(URI faultAddress) {
			FaultAddress = faultAddress;
		}

		public void setNetwork(String network) {
			Network = network;
		}

		public void setExpirationTime(Date expirationTime) {
			ExpirationTime = expirationTime;
		}

		public void setRetryCount(int retryCount) {
			RetryCount = retryCount;
		}

		public void setOriginalMessageId(String originalMessageId) {
			OriginalMessageId = originalMessageId;
		}
        
     

        public void populate(IMessageContext context)
        {
            setMessageType(context.getMessageType());
            setMessageId(context.getMessageId());
            setRequestId(context.getRequestId());
            setConversationId(context.getConversationId());
            setCorrelationId(context.getCorrelationId());
            setSourceAddress(context.getSourceAddress());
            setDestinationAddress(context.getDestinationAddress());
            setResponseAddress(context.getResponseAddress());
            setFaultAddress(context.getFaultAddress());
            setNetwork(context.getNetwork());
                setExpirationTime(context.getExpirationTime());
            setRetryCount(context.getRetryCount());
            setContentType(context.getContentType());

            _headers = new MessageHeaders(context.getHeaders().entrySet());
        }
    }
