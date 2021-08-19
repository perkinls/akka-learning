package com.lp.akka.notes.cluster.wordcountv2;

import akka.actor.*;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pan
 * @title 聚合（aggregates）的服务
 */
public class StatsAggregator extends UntypedActor {

    final int expectedResults;
    final ActorRef replyTo;
    final List<Integer> results = new ArrayList<Integer>();

    public StatsAggregator(int expectedResults, ActorRef replyTo) {
        this.expectedResults = expectedResults;
        this.replyTo = replyTo;
    }

    @Override
    public void preStart() {

        //定义触发发送 [[akka.actor.ReceiveTimeout]] 消息的不活动超时事件。
        getContext().setReceiveTimeout(Duration.create(3, "seconds"));
    }

    @Override
    public void onReceive(Object message) throws Exception, Exception {

        if (message instanceof Integer) {
            Integer wordCount = (Integer) message;
            results.add(wordCount);
            if (results.size() == expectedResults) {
                int sum = 0;
                for (int c : results) {
                    sum += c;
                }
                double meanWordLength = ((double) sum) / results.size();
                replyTo.tell(new StatsMessages.StatsResult(meanWordLength), getSelf());
                getContext().stop(getSelf());
            }


        } else if (message instanceof ReceiveTimeout) {
            replyTo.tell(new StatsMessages.JobFailed("Service unavailable, try again later"), getSelf());
            getContext().stop(getSelf());

        } else {
            unhandled(message);
        }


    }
}
