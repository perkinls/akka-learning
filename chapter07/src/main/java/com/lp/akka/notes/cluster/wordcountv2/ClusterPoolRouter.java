package com.lp.akka.notes.cluster.wordcountv2;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.routing.ClusterRouterPool;
import akka.cluster.routing.ClusterRouterPoolSettings;
import akka.routing.ConsistentHashingPool;

/**
 * @author li.pan
 * @title
 */
public class ClusterPoolRouter extends UntypedActor {


    private ActorRef masterRouter;

    @Override
    public void preStart() throws Exception, Exception {
        int totalInstances = 100; // 创建路由Actor的最大个数
        int maxInstancesPerNode = 3; // 每个节点最大创建目标Actor个数
        boolean allowLocalRoutees = false; // 本地节点是否允许目标Actor
        String useRoles = "compute"; // 表示选择某些指定角色节点创建actor,这在一定程度上可以限制路由目标的节点数


        // 使用pool方式,自定义集群属性创建路由
        masterRouter =
                getContext().actorOf(
                        new ClusterRouterPool(
                                new ConsistentHashingPool(0),
                                new ClusterRouterPoolSettings(
                                        totalInstances, maxInstancesPerNode, allowLocalRoutees, useRoles))
                                .props(Props.create(StatsWorker.class)),
                        "workerRouter3");

    }

    @Override
    public void onReceive(Object message) throws Exception, Exception {

        if (message instanceof StatsMessages.StatsJob) {
            masterRouter.tell(message, getSender());

        }
    }
}
