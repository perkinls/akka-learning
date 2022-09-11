package com.lp.akka.notes.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import com.typesafe.config.Config;
import scala.Option;

/**
 * @author li.pan
 * @title 自定义邮箱类型
 * <p>
 *     使用配置 mailbox-type 指定
 * </p>
 */
public class UdfMailboxType implements MailboxType, ProducesMessageQueue<UdfMessageQueue> {
    // 反射加载
    public UdfMailboxType(ActorSystem.Settings settings, Config config) {

    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new UdfMessageQueue();
    }
}