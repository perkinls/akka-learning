package com.lp.akka.notes

import akka.actor.ActorRef


case class UserRef(actor: ActorRef, name: String)
