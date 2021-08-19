package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.routing.FromConfig;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author li.pan
 * @title Routee 最快响应返回(ScatterGatherFirstCompleted)
 * TODO 验证结果有误！！！
 */
public class RouterScatterGatherFirstActor extends UntypedActor {
    private ActorRef router;

    @Override
    public void onReceive(Object message) throws Exception {
        router.tell(message, ActorRef.noSender());
    }

    @Override
    public void preStart() throws Exception {
        getContext().actorOf(Props.create(FirstWorker1.class), "fw1");
        getContext().actorOf(Props.create(FirstWorker2.class), "fw2");
        router = getContext().actorOf(FromConfig.getInstance().props(), "firstCompRouter");
    }

    public static void main(String[] args) {
        // 配置方式实现 Router,加载自定义配置文件
        Config routerConfig = ConfigFactory.parseResources("router-application.conf");
        ActorSystem system = ActorSystem.create("sys", routerConfig);

        ActorRef master = system.actorOf(Props.create(RouterScatterGatherFirstActor.class), "routerScatterGatherFirstActor");

        Timeout timeout = new Timeout(Duration.create(10, "seconds"));
        Future<Object> fu = Patterns.ask(master, "helloA", timeout);
        fu.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable failure, Object success) throws Throwable {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                System.out.println(df.format(new Date()) + "err: " + failure);
                System.out.println(df.format(new Date()) + "result: " + success);
            }
        }, system.dispatcher());
//        master.tell("Hello", master);
//        ActorRef ref = system.actorOf(Props.create(FirstWorker1.class), "fw0");
//
//        master.tell(new AddRoutee(new ActorRefRoutee(ref)), ActorRef.noSender());
//
//        Future<Object> fu1 = Patterns.ask(master, GetRoutees.getInstance(), timeout);
//        fu1.onComplete(new OnComplete<Object>() {
//            @Override
//            public void onComplete(Throwable failure, Object success) throws Throwable {
//                Routees rs = (Routees) success;
//                List<Routee> routeeList = rs.getRoutees();
//                for (Routee r : routeeList) {
//                    System.out.println("routee: " + r);
//                }
//            }
//        }, system.dispatcher());
//
//        master.tell(new RemoveRoutee(new ActorRefRoutee(ref)), ActorRef.noSender());
    }
}

class FirstWorker1 extends UntypedActor {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Override
    public void onReceive(Object message) throws Exception {
        Thread.sleep(1000);
        System.out.println(getSelf() + "--->" + message + " From: " + getSender() + df.format(new Date()));
        getSender().tell("OK1", getSelf());
    }
}

class FirstWorker2 extends UntypedActor {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Override
    public void onReceive(Object message) throws Exception {
        Thread.sleep(500);
        System.out.println(getSelf() + "--->" + message + " From: " + getSender() + df.format(new Date()));
        getSender().tell("OK2", getSelf());
    }
}