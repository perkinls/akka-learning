package com.lp.akka.notes.clientactor.statepo;

import com.lp.akka.notes.messages.Request;

import java.util.LinkedList;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 状态容器
 * @createTime 2021年04月26日 12:18:00
 * <p>
 * 用于存储消息的状态容器(FSM)
 * </p>
 */
public class EventQueue extends LinkedList<Request> {
}
