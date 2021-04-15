package com.lp.akka.notes.messages;

import java.io.Serializable;

/**
 * @author lipan
 */
public class SetRequest implements Serializable {
    public final String key;
    public final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
