package com.lp.akka.notes;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.lp.akka.notes.messages.GetRequest;
import com.lp.akka.notes.messages.SetRequest;
import scala.compat.java8.FutureConverters;

import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

public class JClient {
    private final ActorSystem system = ActorSystem.create("LocalSystem");
    private final ActorSelection remoteDb;

    public JClient(String remoteAddress) {
        System.out.println("akka.tcp://akkademy@" + remoteAddress + "/user/akkademy-db");
        remoteDb = system.actorSelection("akka.tcp://akkademy@" + remoteAddress + "/user/akkademy-db");

    }

    public CompletionStage set(String key, Object value) {
        return FutureConverters.toJava(ask(remoteDb, new SetRequest(key, value), 2000));
    }

    public CompletionStage<Object> get(String key) {
        return FutureConverters.toJava(ask(remoteDb, new GetRequest(key), 2000));
    }

}
