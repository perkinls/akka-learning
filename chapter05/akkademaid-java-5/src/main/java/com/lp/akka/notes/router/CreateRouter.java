package com.lp.akka.notes.router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.*;
import com.lp.akka.notes.ArticleParseActor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description Actor Pool 的方式来创建 Router
 * @createTime 2021年01月14日 20:32:00
 */
public class CreateRouter extends AbstractActor {
    ActorSystem system = ActorSystem.create("lp_create_router");

    /**
     * 使用pool方式创建Router
     */
    public void poolRouter() {
        ActorSystem actorSystem = ActorSystem.create("lpSys");
        ActorRef routerActorRef = actorSystem.actorOf(
                Props.create(ArticleParseActor.class)
                        .withRouter(new RoundRobinPool(8)
                                // .withSupervisorStrategy(strategy) //使用pool方式可以使用withSupervisorStrategy指定Router对Pool中路由对象的监督策略
                        ), //R表示依次往复循环发送

                "lpRouterActor");
    }

    /**
     * 使用group方式创建Router
     */
    public void groupRouter() {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(ArticleParseActor.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        Router router = new Router(new RoundRobinRoutingLogic(), routees);
    }

}
