package com.lp.akka.notes.messages;

import java.io.Serializable;

/**
 * 未找到key自定义异常
 * @author lipan
 */
public class KeyNotFoundException extends Exception implements Serializable {
    public final String key;

    public KeyNotFoundException(String key) {
        this.key = key;
    }
}
