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
 * @title group 方式创建路由Actor
 * <p>
 * 路由跟forward模式一样，不会改变原始sender.
 * 创建一个路由Actor有两种模式: pool和group
 *      pool: 路由Actor会创建子Actor作为其 Routee 并对其监督与监控,当某个Routee终止时将其移除。
 *      group: 将 Routee 产生的方式昂放在外部(非自包含),然后路由Actor通过path对这些目标进行发送消息。
 * </p>
 */
public class GroupRouterActor extends UntypedActor {
    private ActorRef router = null;

    @Override
    public void preStart() throws Exception {
        getContext().actorOf(Props.create(WorkTask.class), "wt1");
        getContext().actorOf(Props.create(WorkTask.class), "wt2");
        getContext().actorOf(Props.create(WorkTask.class), "wt3");
        router = getContext().actorOf(FromConfig.getInstance().props(), "router");
//        List<String> routeePaths = Arrays.asList("/user/masterActor/wt1", "/user/masterActor/wt2", "/user/masterActor/wt3");
//        router = getContext().actorOf(new RoundRobinGroup(routeePaths).props(), "router");
        System.out.println("router:" + router);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        router.tell(message, getSender());
    }

    public static void main(String[] args) {
        // 配置方式实现group Router,加载自定义配置文件
        Config routerConfig = ConfigFactory.parseResources("router-application.conf");
        ActorSystem system = ActorSystem.create("sys", routerConfig);

        //ActorSystem system = ActorSystem.create("sys");
        ActorRef ref = system.actorOf(Props.create(GroupRouterActor.class), "groupRouterActor");
        ref.tell("HelloA", ActorRef.noSender());
        ref.tell("HelloB", ActorRef.noSender());
        ref.tell("HelloC", ActorRef.noSender());
    }
}

class WorkTask extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println(getSelf() + "-->" + message + "-->" + getContext().parent());
    }
}