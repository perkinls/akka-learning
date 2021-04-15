package com.lp.akka.notes.lifecycle

import akka.actor.{Actor, ActorLogging, Props, ReceiveTimeout}

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 父Actor
 * @createTime 2021年01月13日 13:04:00
 */
class ParentActor extends Actor with ActorLogging {
  println("start pActor ")

  def receive = {
    case "test" => log.info("received test")
    case ("newChild", name: String) => context.actorOf(Props[ChildActor], name)
    case ("stop", name: String) => {
      val child = context.actorFor(self.path + "/" + name);
      context.stop(child)
    }
    case ReceiveTimeout => throw new RuntimeException("received timeout"); // 每隔超时时间没收到消息就抛出异常
    case "suicide" =>
    case x: Any => log.info("received unknown message :" + x)
  }

  /**
   * 在 actor 实例化后执行，重启时不会执行
   */
  override def preStart {
    println("actor:" + self.path + ", parent preStart ")
  }

  /**
   * 在 actor 正常终止后执行，异常重启时不会执行。
   */
  override def postStop {
    println("actor:" + self.path + ",parent postStop .")
  }

  /**
   * 在 actor 异常重启前保存状态
   */
  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("actor:" + self.path + ", preRestart parent, reason:" + reason + ", message:" + message)
  }

  /**
   * 在 actor 异常重启后恢复状态
   */
  override def postRestart(reason: Throwable) {
    println("actor:" + self.path + ", postRestart parent, reason:" + reason)
  }
}
