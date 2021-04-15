package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.lp.akka.notes.messages.SetRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 创建 Actor，并描述 Actor 接收到消息后如何做出响应
 * @createTime 2020年12月16日 13:39:00
 */
public class AkkademyDb extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    protected final Map<String, Object> map = new HashMap<String, Object>();

    private AkkademyDb() {
        /**
         * 在调用的match方法中，我们定义：如果消息的类型是 SetRequest.class，那么接受该消息、打印日志，并且将该Set消息的键和值作为一条新纪录插入到 map 中。
         * 其次，我们捕捉其他所有未知类型的消息，直接输出到日志。
         */
        receive(ReceiveBuilder
                .match(SetRequest.class, message -> {
                    log.info("Received Set request: {}", message);
                    map.put(message.getKey(), message.getValue());
                })
                .matchAny(o -> log.info("received unknown message: {}", o))
                .build()
        );
    }
}
