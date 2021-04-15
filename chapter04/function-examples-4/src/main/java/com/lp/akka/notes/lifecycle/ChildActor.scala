package com.lp.akka.notes.lifecycle

import akka.actor.Actor

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 子 Actor
 * @createTime 2021年01月13日 13:03:00
 */
class ChildActor extends Actor {

  override def receive() = {
    case "abc" => println("get abc string ")
    case "exception" => throw new NullPointerException()
    case _ => println("children cann't handle unknown message")
  }

  override def preStart {
    println("actor:" + self.path + ",child preStart .")
  }

  override def postStop {
    println("actor:" + self.path + ",child postStop .")
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("actor:" + self.path + ",preRestart child, reason:" + reason + ", message:" + message)
  }

  override def postRestart(reason: Throwable) {
    println("actor:" + self.path + ",postRestart child, reason:" + reason)
  }
}
