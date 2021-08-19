package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title PinnedDispatcher 使用
 */
public class ActorDispatcherPinnedDemo extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(getSelf() + "---->" + message + " " + Thread.currentThread().getName());
        Thread.sleep(5000);
    }

    public static void main(String[] args) {
        // 加载自定义的conf文件
        Config pinnedConfig = ConfigFactory.parseResources("dispatcher-pinned-application.conf");
        ActorSystem system = ActorSystem.create("sys", pinnedConfig);

        for (int i = 0; i < 20; i++) {
            /**
             * pinned单独维护线程池,其他情况下Actor共享线程池(顿挫感)
             */
            ActorRef ref = system.actorOf(Props.create(ActorDispatcherPinnedDemo.class)
                    .withDispatcher("my-pinned-dispatcher"),
                    //.withDispatcher("my-forkjoin-dispatcher"),
                    "actorDemo" + i);
            ref.tell("Hello pinned", ActorRef.noSender());
        }
    }
}