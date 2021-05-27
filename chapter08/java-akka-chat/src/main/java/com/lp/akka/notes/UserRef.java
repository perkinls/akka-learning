package com.lp.akka.notes;

import akka.actor.ActorRef;


/**
 * 用户
 * @author lipan
 */
public class UserRef {
    public final ActorRef actor;
    public final String username;

    public UserRef(ActorRef actor, String username) {
        this.actor = actor;
        this.username = username;
    }
}
