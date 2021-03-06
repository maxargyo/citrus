/*
 * Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.message;

import org.springframework.integration.Message;

import com.consol.citrus.TestActor;

/**
 * Basic message sender interface. Message senders are capable of publishing messages to a 
 * specific message endpoint. Each message transport may have dedicated message sender implementations.
 * 
 * @author Christoph Deppisch
 */
public interface MessageSender {
    /**
     * Sends the message.
     * @param message the message object to send.
     */
    void send(Message<?> message);
    
    /**
     * Gets the sending actor.
     * @return
     */
    TestActor getActor();
    
    /**
     * Enumeration representing the different error handling strategies for this
     * reply message handler.
     */
    public enum ErrorHandlingStrategy {
        THROWS_EXCEPTION("throwsException"),
        PROPAGATE("propagateError");
        
        /** Name representation */
        private String name;
        
        /**
         * Default constructor using String name representation field.
         * @param name
         */
        private ErrorHandlingStrategy(String name) {
            this.name = name;
        }
        
        /**
         * Gets the strategy from given name representation.
         * @param name
         * @return
         */
        public static ErrorHandlingStrategy fromName(String name) {
            for (ErrorHandlingStrategy strategy : values()) {
                if (strategy.getName().equals(name)) {
                    return strategy;
                }
            }
            
            throw new IllegalArgumentException("Unknown error handling strategy: " + name);
        }
        
        /**
         * Gets the name representation.
         * @return the name
         */
        public String getName() {
            return name;
        }
    }
}
