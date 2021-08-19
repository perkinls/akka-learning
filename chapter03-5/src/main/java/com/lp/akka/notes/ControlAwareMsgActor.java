package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.lp.akka.notes.config.ControlMsg;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title 控制指令优先消息
 */
public class ControlAwareMsgActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(message);
    }

    public static void main(String[] args) {
        // 加载自定义的conf文件
        Config mailboxConfig = ConfigFactory.parseResources("mailbox-application.conf");

        ActorSystem system = ActorSystem.create("sys", mailboxConfig);
        ActorRef ref = system.actorOf(Props.create(ControlAwareMsgActor.class)
                        .withMailbox("control-aware-mailbox"), // 指定控制优先指令配置项
                "controlAware");
        /**
         * ControlMsg类型先输出
         */
        Object[] messages = {"Java", "c#", new ControlMsg("ServerPage"), "PHP"};
        for (Object msg : messages) {
            ref.tell(msg, ActorRef.noSender());
        }
    }
}