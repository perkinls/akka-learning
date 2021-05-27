package com.lp.akka.notes.messages

import akka.actor.ActorRef

/**
 * @title TODO
 * @author li.pan
 * @version 1.0.0
 * @createTime 2021年04月30日 14:14:00
 *             <p>
 *             $description
 *             </p>
 */
case class GetRequest(key: String, sender: ActorRef = ActorRef.noSender) extends Request
