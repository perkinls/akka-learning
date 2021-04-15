package com.lp.akka.notes.supervisor;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description 模拟醉酒的寿司师监督策略Java版本
 * https://cloud.tencent.com/developer/article/1435518
 *   AKka监督策略
 * @createTime 2021年01月10日 15:26:00
 */
public class JavaSupervisorStrategyDemo extends AbstractActor {
//    @Override
//    public SupervisorStrategy supervisorStrategy() {
//        /*
//         * resume(): Actor 继续处理下一条消息;
//         * restart():  停 止Actor，不再做任何操作;
//         * escalate(): 新建一个 Actor，代替原来的 Actor;
//         * stop(): 将异常信息传递给下一个监督者;
//         */
//        return new OneForOneStrategy(5, Duration.create("1 minute"),
//                akka.japi.pf.DeciderBuilder
//                        .match(BrokenPlateException.class,
//                                e -> SupervisorStrategy.resume())
//                        .match(DrunkenFoolException.class,
//                                e -> SupervisorStrategy.restart())
//                        .match(RestaurantFireError.class,
//                                e -> SupervisorStrategy.escalate())
//                        .match(TiredChefException.class,
//                                e -> SupervisorStrategy.stop())
//                        .matchAny(e -> SupervisorStrategy.escalate()).build());
//    }

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.create(1, TimeUnit.MINUTES),
                    /*
                     * resume(): Actor 继续处理下一条消息;
                     * restart():  停止Actor，不再做任何操作;
                     * escalate(): 新建一个 Actor，代替原来的 Actor;
                     * stop(): 将异常信息传递给下一个监督者;
                     */
                    DeciderBuilder.match(ArithmeticException.class, e -> SupervisorStrategy.resume())
                            .match(NullPointerException.class, e -> SupervisorStrategy.restart())
                            .match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
                            .matchAny(o -> SupervisorStrategy.escalate())
                            .build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}
