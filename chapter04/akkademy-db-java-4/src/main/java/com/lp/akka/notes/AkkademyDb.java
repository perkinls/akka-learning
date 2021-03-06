package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.lp.akka.notes.messages.Connected;
import com.lp.akka.notes.messages.GetRequest;
import com.lp.akka.notes.messages.KeyNotFoundException;
import com.lp.akka.notes.messages.SetRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 模拟数据库server端
 * @author lipan
 */
public class AkkademyDb extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    public final Map<String, Object> map = new HashMap<String, Object>();

    private AkkademyDb() {
        receive(ReceiveBuilder.
                        match(Connected.class, message -> {
                            log.info("Received Connected request: {}", message);
                            sender().tell(new Connected(), self()); // sender()返回所收到消息的响应
                        }).
                        match(List.class, message -> {
                                    message.forEach(x -> {
                                        if (x instanceof SetRequest) {
                                            SetRequest setRequest = (SetRequest) x;
                                            handleSetRequest(setRequest);
                                        }
                                        if (x instanceof GetRequest) {
                                            GetRequest getRequest = (GetRequest) x;
                                            handleGetRequest(getRequest);
                                        }
                                    });
                                }
                        ).
                        match(SetRequest.class, message -> {
                            handleSetRequest(message);
                        }).
                        match(GetRequest.class, message -> {
                            handleGetRequest(message);
                        }).
                        matchAny(o -> {
                            log.info("unknown message: " + o);
                            sender().tell(new Status.Failure(new ClassNotFoundException()), self());
                        }).build()
        );
    }

    private void handleSetRequest(SetRequest message) {
        log.info("Received Set request: {}", message);
        map.put(message.key, message.value);
        message.sender.tell(new Status.Success(message.key), self());
    }

    private void handleGetRequest(GetRequest getRequest) {
        log.info("Received Get request: {}", getRequest);
        Object value = map.get(getRequest.key);
        Object response = (value != null)
                ? value
                : new Status.Failure(new KeyNotFoundException(getRequest.key));
        sender().tell(response, self());
    }
}