package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 初识Akka Actor
 * <p>
 * UntypedActor：基于经典的Actor模型实现，能完整表达Akka-Actor的设计思想，推荐使用它来定义Actor。
 * TypedActor：会比较方便地把正常OOP的代码包装成异步执行的Actor，比较符合程序员的API调用逻辑，但是不太能表达基于消息的处理方式，在一般情况下，不推荐使用它。
 * </p>
 */
public class ActorDemo extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            // 使用LoggingAdapter对接受到的日志输出
            log.info(message.toString());
        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        // 获取到的是ActorRef, actor引用 (不能通过new获取)
        ActorRef actorRef = system.actorOf(Props.create(ActorDemo.class), "actorDemo");
        actorRef.tell("Hello Akka", ActorRef.noSender());
    }
}
