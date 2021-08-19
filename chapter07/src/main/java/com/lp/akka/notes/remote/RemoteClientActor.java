package com.lp.akka.notes.remote;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

/**
 * @author li.pan
 * @title Akka remote
 */
public class RemoteClientActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            if ("talk".equals(message)) {
                ActorSelection selection = getContext().actorSelection("akka.tcp://sys@127.0.0.1:2552/user/remoteActor");
                // Identify 用于验证身份笑嘻嘻
                selection.tell(new Identify("666"), getSelf());
            } else {
                System.out.println(message);
            }
        } else if (message instanceof ActorIdentity) {
            ActorIdentity ai = (ActorIdentity) message;
            ActorRef actorRef = ai.getRef();
            if (actorRef != null) {
                actorRef.tell("Hello remoteActor", getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * 测试需提前启动 {@link com.lp.akka.notes.remote.RemoteCliDemo}
     * @param args
     */
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("sysCli", ConfigFactory.load("remote-cli-application.conf") );
        ActorRef actorRef = system.actorOf(Props.create(RemoteClientActor.class), "cliActor");
        actorRef.tell("talk", ActorRef.noSender());
    }
}
