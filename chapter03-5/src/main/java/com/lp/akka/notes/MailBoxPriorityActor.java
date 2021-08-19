package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title 使用自定义邮箱(优先级)
 */
public class MailBoxPriorityActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(message);
    }

    public static void main(String[] args) {
        // 加载自定义的conf文件
        Config mailboxConfig = ConfigFactory.parseResources("mailbox-application.conf");

        ActorSystem system = ActorSystem.create("sys", mailboxConfig);
        ActorRef ref = system.actorOf(Props.create(MailBoxPriorityActor.class)
                        .withMailbox("msgprio-mailbox"), // 指定优先级配置项
                "priorityActor");
        Object[] messages = {"王五", "李四", "张三", "小二"};
        for (Object msg : messages) {
            ref.tell(msg, ActorRef.noSender());
        }
    }
}
