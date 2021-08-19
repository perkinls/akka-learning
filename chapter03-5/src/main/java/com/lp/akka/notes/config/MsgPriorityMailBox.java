package com.lp.akka.notes.config;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;

/**
 * @author li.pan
 * @title 自定义消息优先级
 * <p>
 * 实现 UnboundedStablePriorityMailbox 会对于同一优先级的消息按照先进先出 (FIFO) 的顺序进行处理。
 * 通过 Mailbox-Type 绑定。
 * </p>
 */
public class MsgPriorityMailBox extends UnboundedStablePriorityMailbox {


    /**
     * 重写gen方法
     *
     * @param settings
     * @param config
     */
    public MsgPriorityMailBox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                // 返回值越小优先级越高
                if (message.equals("张三")) {
                    return 0;
                } else if (message.equals("李四")) {
                    return 1;
                } else if (message.equals("王五")) {
                    return 2;
                } else {
                    return 3;
                }
            }
        });
    }
}