package com.lp.akka.notes.cluster.wordcountv2;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ConsistentHashingRouter;
import akka.routing.FromConfig;

/**
 * @author li.pan
 * @title
 */
public class StatsService extends UntypedActor {

    // This router is used both with lookup and deploy of routees. If you
    // have a router with only lookup of routees you can use Props.empty()
    // instead of Props.create(StatsWorker.class).
    ActorRef workerRouter =
            getContext()
                    .actorOf(FromConfig.getInstance().props(Props.create(StatsWorker.class)), "workerRouter");

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof StatsMessages.StatsJob) {
            StatsMessages.StatsJob job = (StatsMessages.StatsJob) message;
            if (!job.getText().isEmpty()) {
                String[] words = job.getText().split(" ");
                ActorRef replyTo = getSelf();
                // create actor that collects replies from workers
                ActorRef aggregator =
                        getContext().actorOf(Props.create(StatsAggregator.class, words.length, replyTo));

                // send each word to a worker
                for (String word : words) {
                    workerRouter.tell(new ConsistentHashingRouter.ConsistentHashableEnvelope(word, word), aggregator);
                }

            }


        }
    }

}
