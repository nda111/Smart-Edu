package com.gachon.smartedu.messaging;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * A data class that holds the message informations
 *
 * @author 유근혁
 * @since June 22rd 2020
 */
public final class Message {

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FROM = "from";
    private static final String KEY_FROM_EMAIL = "email";
    private static final String KEY_WHEN = "when";
    private static final String KEY_CONTENT = "content";

    /**
     * Unpack data from {Bundle}
     *
     * @param bundle The bundle instance to unpack
     * @return Message instance if success, {null} otherwise
     */
    public static Message unpack(@NonNull Bundle bundle) {
        if (!(bundle.containsKey(KEY_ID) &&
                bundle.containsKey(KEY_TITLE) &&
                bundle.containsKey(KEY_FROM) &&
                bundle.containsKey(KEY_FROM_EMAIL) &&
                bundle.containsKey(KEY_WHEN) &&
                bundle.containsKey(KEY_CONTENT))) {
            return null;
        }

        return new Message(
                bundle.getLong(KEY_ID),
                Objects.requireNonNull(bundle.getString(KEY_FROM)),
                Objects.requireNonNull(bundle.getString(KEY_FROM_EMAIL)),
                Objects.requireNonNull(bundle.getString(KEY_TITLE)),
                Objects.requireNonNull(bundle.getString(KEY_CONTENT)),
                bundle.getLong(KEY_WHEN));
    }

    private long id;
    private String from;
    private String fromEmail;
    private String title;
    private String content;
    private long when;

    /**
     * Create an instance
     */
    public Message(long id, @NonNull String from, @NonNull String fromEmail, @NonNull String title, @NonNull String content, long when) {
        this.id = id;
        this.from = from;
        this.fromEmail = fromEmail;
        this.title = title;
        this.content = content;
        this.when = when;
    }

    /**
     * Get the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Get the name of sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get the email address of the sender
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * Get the title of the message
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the time when the message was sent in milliseconds
     */
    public long getWhen() {
        return when;
    }

    /**
     * Pack this message instance into a bundle with specified key
     */
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();

        bundle.putLong(KEY_ID, id);
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_FROM, from);
        bundle.putString(KEY_FROM_EMAIL, fromEmail);
        bundle.putString(KEY_CONTENT, content);
        bundle.putLong(KEY_WHEN, when);

        return bundle;
    }
}
