package com.lp.akka.notes.supervisor;

import akka.actor.AbstractActor;
import akka.actor.IllegalActorStateException;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

import java.util.concurrent.TimeUnit;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年01月11日 13:22:00
 */
public class Supervisor extends AbstractActor {

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.create(1, TimeUnit.MINUTES),
                    DeciderBuilder.match(ArithmeticException.class, e -> SupervisorStrategy.resume())
                            .match(NullPointerException.class, e -> SupervisorStrategy.restart())
                            .match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
                            .matchAny(o -> SupervisorStrategy.escalate())
                            .build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }


//    @Override
//    public Receive createReceive() {
//        return receiveBuilder()
//                .match(
//                        Props.class,
//                        props -> {
//                            getSender().tell(getContext().actorOf(props), getSelf());
//                        })
//                .build();
//    }


}