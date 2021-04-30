package com.lp.akka.notes.dispatcher;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.lp.akka.notes.ArticleParser;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 创建Dispatcher
 * @createTime 2021年04月26日 17:52:00
 * <p>
 *
 * </p>
 */
public class CreateDispatcher extends AbstractActor {
    public void createDispatcher() {
        ActorSystem system = ActorSystem.create("lp_create_router");
        ActorRef actor = system.actorOf(Props.create(ArticleParser.class), "akkademy-db");
    }

}
