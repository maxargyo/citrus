/*
 * Copyright 2006-2012 the original author or authors.
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
package com.consol.citrus.channel.selector;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
public class DispatchingMessageSelectorTest {

    @Test
    public void testHeaderMatchingSelector() {
        DispatchingMessageSelector messageSelector = new DispatchingMessageSelector("operation = 'foo'");
        
        Message<String> acceptMessage = MessageBuilder.withPayload("FooTest")
                .setHeader("operation", "foo")
                .build();
        
        Message<String> declineMessage = MessageBuilder.withPayload("FooTest")
                .setHeader("operation", "foobar")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
    }
    
    @Test
    public void testHeaderMatchingSelectorAndOperation() {
        DispatchingMessageSelector messageSelector = new DispatchingMessageSelector("foo = 'bar' AND operation = 'foo'");
        
        Message<String> acceptMessage = MessageBuilder.withPayload("FooTest")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        Message<String> declineMessage = MessageBuilder.withPayload("FooTest")
                .setHeader("operation", "foo")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
    }
    
    @Test
    public void testRootQNameDelegation() {
        DispatchingMessageSelector messageSelector = new DispatchingMessageSelector("foo = 'bar' AND root-qname = 'FooTest'");
        
        Message<String> acceptMessage = MessageBuilder.withPayload("<FooTest><text>foobar</text></FooTest>")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        Message<String> declineMessage = MessageBuilder.withPayload("<BarTest><text>foobar</text></BarTest>")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
        
        messageSelector = new DispatchingMessageSelector("root-qname = 'FooTest'");
        
        acceptMessage = MessageBuilder.withPayload("<FooTest><text>foobar</text></FooTest>")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        declineMessage = MessageBuilder.withPayload("<BarTest><text>foobar</text></BarTest>")
                .setHeader("operation", "foo")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
    }
    
    @Test
    public void testRootQNameDelegationWithNamespace() {
        DispatchingMessageSelector messageSelector = new DispatchingMessageSelector("root-qname = '{http://citrusframework.org/fooschema}FooTest'");
        
        Message<String> acceptMessage = MessageBuilder.withPayload("<FooTest xmlns=\"http://citrusframework.org/fooschema\"><text>foo</text></FooTest>")
                .setHeader("operation", "foo")
                .build();
        
        Message<String> declineMessage = MessageBuilder.withPayload("<FooTest xmlns=\"http://citrusframework.org/barschema\"><text>bar</text></FooTest>")
                .setHeader("operation", "foo")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
    }
    
    @Test
    public void testXPathEvaluationDelegation() {
        DispatchingMessageSelector messageSelector = new DispatchingMessageSelector("foo = 'bar' AND root-qname = 'FooTest' AND xpath://FooTest/text = 'foobar'");
        
        Message<String> acceptMessage = MessageBuilder.withPayload("<FooTest><text>foobar</text></FooTest>")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        Message<String> declineMessage = MessageBuilder.withPayload("<FooTest><text>barfoo</text></FooTest>")
                .setHeader("foo", "bar")
                .setHeader("operation", "foo")
                .build();
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
        
        messageSelector = new DispatchingMessageSelector("xpath://FooTest/text = 'foobar'");
        
        Assert.assertTrue(messageSelector.accept(acceptMessage));
        Assert.assertFalse(messageSelector.accept(declineMessage));
    }
}
