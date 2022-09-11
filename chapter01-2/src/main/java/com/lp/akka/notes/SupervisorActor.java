package com.lp.akka.notes;

import akka.actor.*;
import akka.dispatch.OnSuccess;
import akka.japi.Function;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.Option;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author li.pan
 * @title Actor 容错处理
 */
public class SupervisorActor extends UntypedActor {

    ActorRef workerActor2 = null;

    @Override
    public void preStart() throws Exception {
        workerActor2 = getContext().actorOf(Props.create(WorkerActor2.class), "workerActor2");
        getContext().watch(workerActor2);
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {

        /*
         * 自定义Actor One-For-One监督策略
         * 在一分钟内重启3次失败将停止
         */
        return new OneForOneStrategy(3, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                if (t instanceof IOException) {
                    System.out.println("========== IOException =========");
                    // 恢复运行
                    return SupervisorStrategy.resume();
                } else if (t instanceof IndexOutOfBoundsException) {
                    System.out.println("========== IndexOutOfBoundsException =========");
                    // 重启
                    return SupervisorStrategy.restart();
                } else if (t instanceof SQLException) {
                    System.out.println("========== SQLException =========");
                    // 停止
                    return SupervisorStrategy.stop();
                } else {
                    System.out.println("========== escalate =========");
                    // 失败策略向上升级
                    return SupervisorStrategy.escalate();
                }
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        workerActor2.tell(message, self());

        if (message instanceof Terminated) {
            Terminated ter = (Terminated) message;
            System.out.println(ter.getActor() + "已经终止");
        } else {
            System.out.println("stateCount=" + message);
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef workerRef = actorSystem.actorOf(Props.create(SupervisorActor.class), "supervsior");
        workerRef.tell(new IOException(), ActorRef.noSender());
//        workerRef.tell(new SQLException("SQL异常"), ActorRef.noSender());
//        workerRef.tell(new IndexOutOfBoundsException(),ActorRef.noSender());
//        workerRef.tell("get value", ActorRef.noSender());
    }
}
class WorkerActor2 extends UntypedActor {
    private int stateCount = 1;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        System.out.println("worker actor preStart");
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        System.out.println("worker actor postStop");
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println("worker actor preRestart begin " + this.stateCount);
        super.preRestart(reason, message);
        System.out.println("work actor preRestart end " + this.stateCount);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        System.out.println("worker actor postRestart begin " + this.stateCount);
        super.postRestart(reason);
        System.out.println("worker actor postRestart end " + this.stateCount);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        this.stateCount++;
        if (message instanceof Exception) {
            System.out.println("worker stateCount: " + stateCount);
            throw (Exception) message;
        } else if ("getValue".equals(message)) {
            System.out.println("worker stateCount: " + stateCount);
            getSender().tell(stateCount, getSelf());
        } else {
            System.out.println("worker stateCount: " + stateCount);
            unhandled(message);
        }
    }
}