package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title 广播路由器,会给所有指定的routee发送消息
 */
public class RouterBroadCastActor extends UntypedActor {
    private ActorRef router;

    @Override
    public void onReceive(Object message) throws Exception {
        router.tell(message, ActorRef.noSender());
    }

    @Override
    public void preStart() throws Exception {
        getContext().actorOf(Props.create(BroadWorker1.class), "bw1");
        getContext().actorOf(Props.create(BroadWorker2.class), "bw2");
        router = getContext().actorOf(FromConfig.getInstance().props(), "broadRouter");
    }

    public static void main(String[] args) {
        // 配置方式实现pool Router,加载自定义配置文件
        Config routerConfig = ConfigFactory.parseResources("router-application.conf");
        ActorSystem system = ActorSystem.create("sys", routerConfig);

        ActorRef master = system.actorOf(Props.create(RouterBroadCastActor.class), "routerBroadActor");
        master.tell("helloA", ActorRef.noSender());
        master.tell("helloB", ActorRef.noSender());
    }
}

class BroadWorker1 extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(getSelf() + "--->" + message);
    }
}

class BroadWorker2 extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(getSelf() + "--->" + message);
    }
}