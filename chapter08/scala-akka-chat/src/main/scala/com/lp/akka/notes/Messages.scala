package com.lp.akka.notes

case class JoinChatroom(userRef: UserRef)
case class PostToChatroom(line: String, username: String)