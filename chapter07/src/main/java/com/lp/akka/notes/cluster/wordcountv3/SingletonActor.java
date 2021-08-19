package com.lp.akka.notes.cluster.wordcountv3;

import akka.actor.UntypedActor;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;

/**
 * @author li.pan
 * @title 单例Actor
 */
public class SingletonActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception, Exception {
        // 打印MemberUp信息
        if (message instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp mu = (ClusterEvent.MemberUp)message;
            Member m = mu.member();
            System.out.println(m + " is up");
        } else {
            unhandled(message);
        }
    }
}