package com.lp.akka.notes.config;

import akka.dispatch.ControlMessage;

/**
 * @author li.pan
 * @title 控制优先指令
 */
public class ControlMsg implements ControlMessage {
    private final String status;

    public ControlMsg(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
