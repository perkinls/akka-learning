package com.lp.akka.notes.messages;

import java.io.Serializable;

/**
 * @author lipan
 */
public class KeyNotFoundException extends Exception implements Serializable {
    public final String key;

    public KeyNotFoundException(String key) {
        this.key = key;
    }
}
