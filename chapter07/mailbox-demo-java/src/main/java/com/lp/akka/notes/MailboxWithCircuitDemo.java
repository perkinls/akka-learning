package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.CircuitBreaker;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import static scala.compat.java8.FutureConverters.toJava;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 信箱及熔断机制示例
 * @createTime 2021年01月21日 22:42:00
 */
public class MailboxWithCircuitDemo {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        //  通过Props方式创建Actor,通过withMailbox指定邮箱类型
        ActorRef pingActorWithMailbox = system.actorOf(
                PingActor.props().withMailbox("akka.actor.boundedmailbox"),
                "pingActor");

        // 使用tell方式发送消息     (接收一个响应地址作为参数，接收消息的 Actor 中的 sender()就是这个响应地址)
        pingActorWithMailbox.tell(new PingActor.Initialize(), null);

        ActorRef pongActor = system.actorOf(
                PingActor.props().withMailbox("akka.actor.boundedmailbox"),
                "pingActor2");

        // 熔断机制
        CircuitBreaker breaker =
                new CircuitBreaker(system.scheduler(),
                        1, // 熔断器熔断之前发生错误的最大次数（无论是超时还是 Future 返回失败） ；
                        FiniteDuration.create(1, "second"),// 调用超时（延时超过多长时间就熔断） ；
                        FiniteDuration.create(1, "second"),// 重置超时（等待多久以后将状态改为“半开” ，并尝试发送一个请求）
                        system.dispatcher())
                        .onOpen(() -> {
                            System.out.println("circuit breaker opened!");
                        })
                        .onClose(() -> {
                            System.out.println("circuit breaker closed!");
                        }).onHalfOpen(() -> {
                            System.out.println("circuit breaker half opened!");
                });

        Timeout timeout = Timeout.longToTimeout(2000L);

        Future future1 = breaker.callWithCircuitBreaker(()
                -> Patterns.ask(pongActor, new PingActor.PingMessage("ping"), timeout));

        Future future2 = breaker.callWithCircuitBreaker(()
                -> Patterns.ask(pongActor, new PingActor.PingMessage("ping"), timeout));


        toJava(future1).handle((x, t) -> {
            if (t != null) {
                System.out.println("got it: " + x);
            } else {
                System.out.println("error: " + t.toString());
            }
            return null;
        });

        toJava(future2).handle((x, t) -> {
            if (t != null) {
                System.out.println("got it: " + x);
            } else {
                System.out.println("error: " + t.toString());
            }
            return null;
        });

        //play around with sending futures and see how the breaker responds

        system.awaitTermination();
    }
}
