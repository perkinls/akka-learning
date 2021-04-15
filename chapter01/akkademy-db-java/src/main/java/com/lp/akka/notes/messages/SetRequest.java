package com.lp.akka.notes.messages;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 消息体
 * @createTime 2020年12月16日 13:28:00
 */
public class SetRequest {
    private final String key;
    private final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
