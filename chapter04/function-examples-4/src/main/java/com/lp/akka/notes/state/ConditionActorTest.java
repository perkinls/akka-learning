//package com.lp.akka.notes.state;
//
//import akka.actor.AbstractActor;
//import akka.io.Tcp;
//import scala.PartialFunction;
//
///**
// * @author li.pan
// * @version 1.0.0
// * @Description 条件语句
// * @createTime 2021年01月13日 21:21:00
// */
//public class ConditionActorTest extends AbstractActor {
//    when(DISCONNECTED,
//         matchEvent(FlushMsg.class, (msg, container) -> stay())
//            .event(GetRequest.class, (msg, container) -> {
//                container.add(msg);
//                return stay();
//            })
//            .event(Tcp.Connected.class, (msg, container) -> {
//                if(container.getFirst() == null) {
//                    return goTo(CONNECTED);
//                } else {
//                    return goTo(CONNECTED_AND_PENDING);
//                }
//            }));
//    when(CONNECTED,
//         matchEvent(FlushMsg.class, (msg, container) -> stay()) {
//            .event(GetRequest.class, (msg, container) -> {
//                container.add(msg);
//                return goTo(CONNECTED_AND_PENDING);
//        }));
//    when(CONNECTED_AND_PENDING,
//         matchEvent(FlushMsg.class, (msg, container) -> {
//                container = new EventQueue();
//                return stay();
//         })
//         .event(GetRequest.class, (msg, container) -> {
//                container.add(msg);
//                return goTo(CONNECTED_AND_PENDING);
//         }));
//
//    scala.PartialFunction pf = ReceiveBuilder.match(String.class,
//                x -> System.out.println(x)).build();
//    when(CONNECTED, pf);
//}