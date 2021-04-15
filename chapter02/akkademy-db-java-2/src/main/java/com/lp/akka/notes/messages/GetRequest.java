package com.lp.akka.notes.messages;

import java.io.Serializable;

/**
 * @author lipan
 */
public class GetRequest implements Serializable {
    public final String key;

    public GetRequest(String key) {
        this.key = key;
    }
}
