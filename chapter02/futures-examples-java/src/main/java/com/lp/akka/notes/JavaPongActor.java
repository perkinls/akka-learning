package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;

/**
 * @author lipan
 */
public class JavaPongActor extends AbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);

    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.matchEquals("Ping", message -> {
            sender().tell("Pong", self());
            log.info("message:" + message);
            log.info("sender:" + sender().path());
            log.info("self:" + self());
        }).matchAny(other -> {
            sender().tell(new Status.Failure(new Exception("unknown message")), self());
            log.info("other:" + other);
        }).build();
    }
}
