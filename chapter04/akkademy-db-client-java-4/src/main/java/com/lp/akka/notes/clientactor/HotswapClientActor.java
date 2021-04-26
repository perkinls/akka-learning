package com.lp.akka.notes.clientactor;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;
import com.lp.akka.notes.messages.Connected;
import com.lp.akka.notes.messages.Request;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;


/**
 * @author li.pan
 * @version 1.0.0
 * @title 状态管理-----热交换(Become/Unbecome)
 * @createTime 2021年04月26日 12:18:00
 * <p>
 * 每个状态的行为都定义在自己独立的PartialFunction中。
 * become(PartialFunction behavior)：这个方法将 receive 块中定义的行为修改为一个新的 PartialFunction。
 * unbecome()：这个方法将 Actor 的行为修改回默认行为
 * </p>
 */
public class HotswapClientActor extends AbstractActorWithStash {
    private ActorSelection remoteDb;

    private PartialFunction<Object, BoxedUnit> disconnected;
    private PartialFunction<Object, BoxedUnit> online;

    public HotswapClientActor(String dbPath) {
        remoteDb = context().actorSelection(dbPath);

        /*
         * 在接收到 Connected 消息之前，这些消息都会被暂存起来放在一边。
         */
        disconnected = ReceiveBuilder.
                match(Request.class, x -> { //can't handle until we know remote system is responding
                    remoteDb.tell(new Connected(), self()); //see if the remote actor is up
                    stash(); // 缓存消息
                }).
                match(Connected.class, x -> { // Okay to start processing messages.
                    context().become(online);
                    unstash(); //获取消息
                }).build();

        /*
         * 一旦接收到Connected 消息，Actor就调用become，将状态修改为在线（定义在 online 方法中） 。
         * 此时， Actor还会调用unstash将所有暂存的消息取回到工作队列中。 这样就可以使用online方法中定义的行为来处理所有的消息了。
         */
        online = ReceiveBuilder.
                        match(Request.class, x -> {
                            remoteDb.forward(x, context()); //forward instead of tell to preserve sender
                        }).
                        build();

        /**
         * 可以定义任意数量的 receive 块，并相互切换
         */
        receive(disconnected); //初始化状态
    }
}

