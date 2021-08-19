package com.lp.akka.notes.config;

import akka.actor.ActorRef;
import akka.dispatch.Envelope;
import akka.dispatch.MessageQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author li.pan
 * @title 自定义邮箱队列
 */
public class UdfMessageQueue implements MessageQueue {
    /**
     * 使用ConcurrentLinkedQueue作为邮箱队列
     */
    private Queue<Envelope> queue = new ConcurrentLinkedQueue<Envelope>();

    @Override
    public void enqueue(ActorRef receiver, Envelope el) {
        queue.offer(el);
    }

    @Override
    public Envelope dequeue() {
        return queue.poll();
    }

    @Override
    public int numberOfMessages() {
        return queue.size();
    }

    @Override
    public boolean hasMessages() {
        return !queue.isEmpty();
    }

    @Override
    public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
        for (Envelope el : queue) {
            deadLetters.enqueue(owner, el);
        }
    }
}