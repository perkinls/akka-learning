package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title 自定义邮箱Actor
 */
public class MailBoxUdfActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception, Exception {
        System.out.println(message);
    }

    public static void main(String[] args) {

        // 加载自定义的conf文件
        Config mailboxConfig = ConfigFactory.parseResources("mailbox-application.conf");

        ActorSystem system = ActorSystem.create("sys", mailboxConfig);
        ActorRef ref = system.actorOf(Props.create(MailBoxUdfActor.class).withMailbox("udf-mailbox"), "udfActor");
        ref.tell("aaaa", ActorRef.noSender());
    }
}
