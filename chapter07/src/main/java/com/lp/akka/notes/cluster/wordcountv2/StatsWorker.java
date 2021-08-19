package com.lp.akka.notes.cluster.wordcountv2;

import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li.pan
 * @title 计算每个字中字符数的工作者（worker）
 */
public class StatsWorker extends UntypedActor {

    Map<String, Integer> cache = new HashMap<String, Integer>();

    @Override
    public void onReceive( Object message) throws Exception, Exception {

        if (message instanceof String) {
            String word = (String)message;
            Integer length = cache.get(word);
            if (length == null) {
                length = word.length();
                cache.put(word, length);
            }
            getSender().tell(length, getSelf());
        }
    }
}
