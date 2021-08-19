package com.lp.akka.notes.cluster.wordcountv1;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pan
 * @title 单词统计前端服务
 * <p>
 *     前端服务节点通过(JobCount)对"可服务列表"的个数(NodeSize)取余数
 * </p>
 */
public class WordFrontService extends UntypedActor {
    private List<ActorRef> wordCountServices = new ArrayList<>();

    private int jobCounter = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Article) {
            jobCounter++;
            Article art = (Article)message;
            int serviceNodeIndex = jobCounter % wordCountServices.size();
            System.out.println("选择节点：" + serviceNodeIndex);
            wordCountServices.get(serviceNodeIndex).forward(art, getContext());
        }
        /*
         * 根据后段actor返回的消息,维护可以使用的actor列表
         */
        else if (message instanceof String) {
            String cmd = (String)message;
            if (cmd.equals("serviceIsOK")) {
                ActorRef backendSender = getSender();
                System.out.println(backendSender + " 可用");
                wordCountServices.add(backendSender);
                // 注册,添加对其观察
                getContext().watch(backendSender);
            } else if (cmd.equals("isReady")) {
                if (!wordCountServices.isEmpty()) {
                    getSender().tell("ready", getSelf());
                } else {
                    getSender().tell("notReady", getSelf());
                }
            }
        } else if (message instanceof Terminated) {
            Terminated ter = (Terminated)message;
            System.out.println("移除了 " + ter.getActor());
            wordCountServices.remove(ter.getActor());
        } else {
            unhandled(message);
        }
    }
}
