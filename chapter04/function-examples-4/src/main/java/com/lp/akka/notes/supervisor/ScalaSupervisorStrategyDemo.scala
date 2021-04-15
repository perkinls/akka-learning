//package com.lp.akka.notes.supervisor
//
//import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
//import akka.actor.{Actor, OneForOneStrategy}
//import com.lp.akka.notes.exception.{BrokenPlateException, DrunkenFoolException, RestaurantFireError, TiredChefException}
//
///**
// * @author li.pan
// * @version 1.0.0
// * @Description 模拟醉酒的寿司师监督策略Scala版本
// * @createTime 2021年01月10日 15:35:00
// */
//class ScalaSupervisorStrategyDemo extends Actor {
//
//
//  override def supervisorStrategy = {
//    OneForOneStrategy() {
//      /*
//       * resume(): Actor 继续处理下一条消息;
//       * restart():  停 止Actor，不再做任何操作;
//       * escalate(): 新建一个 Actor，代替原来的 Actor;
//       * stop(): 将异常信息传递给下一个监督者;
//       */
//      case BrokenPlateException => Resume
//      case DrunkenFoolException => Restart
//      case RestaurantFireError => Escalate
//      case TiredChefException => Stop
//      case _ => Escalate
//    }
//  }
//
//  override def receive: Receive = ???
//}
