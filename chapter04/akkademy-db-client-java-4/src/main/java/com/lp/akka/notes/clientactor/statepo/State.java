package com.lp.akka.notes.clientactor.statepo;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 枚举值定义状态
 * @createTime 2021年04月26日 12:18:00
 * <p>
 * 定义了三种状态
 * </p>
 */
public enum State{
    DISCONNECTED,
    CONNECTED,
    CONNECTED_AND_PENDING
}
