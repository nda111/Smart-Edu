package com.gachon.smartedu.messaging;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * A test that checks {com.gachon.smartedu.messaging.Message} class
 *
 * @author 유근혁
 * @since June 23rd 2020
 */
@RunWith(JUnit4.class)
public class MessageTest {

    /**
     * Test the constructor, getter methods
     */
    @Test
    public void testConstruction() {
        final Message message = new Message(-1, "from", "from_email", "title", "content", 10);

        assert message.getId() == -1;
        assert message.getFrom().equals("from");
        assert message.getFromEmail().equals("from_email");
        assert message.getTitle().equals("title");
        assert message.getContent().equals("content");
        assert message.getWhen() == 10;
    }
}
