package com.lp.akka.notes

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, UnreachableMember}
import akka.event.Logging

/**
 *
 */
class ClusterController extends Actor {
  val log = Logging(context.system, this)

  // 获取cluster对象
  val cluster = Cluster(context.system)

  override def preStart() {
    // 订阅感兴趣的事件
    cluster.subscribe(self, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop() {
    // 取消订阅事件，防止泄漏。
    cluster.unsubscribe(self)
  }

  override def receive = {
    case x: MemberEvent => log.info("MemberEvent: {}", x)
    case x: UnreachableMember => log.info("UnreachableMember {}: ", x)
  }
}

