package com.lp.akka.notes.clientactor;

import akka.actor.AbstractFSM;
import akka.actor.ActorSelection;
import com.lp.akka.notes.clientactor.statepo.EventQueue;
import com.lp.akka.notes.clientactor.statepo.FlushMsg;
import com.lp.akka.notes.clientactor.statepo.State;
import com.lp.akka.notes.messages.Connected;
import com.lp.akka.notes.messages.Request;

import static com.lp.akka.notes.clientactor.statepo.State.*;

/**
 * @author li.pan
 * @version 1.0.0
 * @title 有限自动机（Finite State Machine，FSM）
 * @createTime 2021年04月26日 12:18:00
 * <p>
 * 跟热交换比起来，FSM 是一个更重量级的抽象概念，需要更多的代码和类型才能够实现并运行。
 * 要恢复到断开连接，我们可以发送偶尔的心跳ping，并通过重新启动参与者(抛出一个异常)恢复。
 * </p>
 */
public class FSMClientActor extends AbstractFSM<State, EventQueue> {

    private ActorSelection remoteDb;

    public FSMClientActor(String dbPath) {
        remoteDb = context().actorSelection(dbPath);
    }

    // 用于初始化的代码块
    {
        startWith(DISCONNECTED, new EventQueue()); // 定义Actor如何启动


        /*
         * 定义不同状态对不同消息的响应方法以及如何根据接收到的消息切换状态。
         * when(S state, PartialFunction pf)
         *
         * matchEvent创建一个[[akka.japi.pf.FSMStateFunctionBuilder]]和第一个case语句集。匹配事件、数据类型和谓词的case语句。
         *
         */
        when(DISCONNECTED, // Disconnected 状态会保存消息或是转移到 Connected 状态。它会忽略除了 Connected 和 GetRequest 以外的所有消息。
                matchEvent(FlushMsg.class, (msg, container) -> stay())
                        .event(Request.class, (msg, container) -> {
                            remoteDb.tell(new Connected(), self());
                            container.add(msg);
                            return stay();
                        })
                        .event(Connected.class, (msg, container) -> {
                            if (container.size() == 0) {
                                return goTo(CONNECTED);
                            } else {
                                return goTo(CONNECTED_AND_PENDING);
                            }
                        }));

        when(CONNECTED, //Connected 状态只关心那些会将状态转移到 ConnectedAnd Pending 状态的消息。
                matchEvent(FlushMsg.class, (msg, container) -> stay())
                        .event(Request.class, (msg, container) -> {
                            container.add(msg);
                            return goTo(CONNECTED_AND_PENDING);
                        }));


        when(CONNECTED_AND_PENDING, // ConnectedAndPending 状态可以把容器中的所有请求都发送出去， 也可以向容器中再添加一个请求。
                matchEvent(FlushMsg.class, (msg, container) -> {
                    remoteDb.tell(container, self());
                    container = new EventQueue();
                    return goTo(CONNECTED);
                })
                        .event(Request.class, (msg, container) -> {
                            container.add(msg);
                            return goTo(CONNECTED_AND_PENDING);
                        }));

        initialize();
    }
}