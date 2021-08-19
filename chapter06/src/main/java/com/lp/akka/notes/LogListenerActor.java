package com.lp.akka.notes;

import akka.actor.UntypedActor;
import akka.event.Logging;

/**
 * @author li.pan
 * @title 自定义日志监听器, 仅仅针对不同级别的日志处理
 * 可以通过resource下配置文件方式实现集成slf4j,还可以使用DiagnosticLoggingAdapter在日志这自定义tag
 */
public class LogListenerActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Logging.InitializeLogger) {
            System.out.println("init: " + message);
            getSender().tell(Logging.loggerInitialized(), getSelf());
        } else if (message instanceof Logging.Error) {
            System.out.println("error: " + message);
        } else if (message instanceof Logging.Warning) {
            System.out.println("warn: " + message);
        } else if (message instanceof Logging.Info) {
            System.out.println("info: " + message);
        } else if (message instanceof Logging.Debug) {
            System.out.println("debug: " + message);
        } else {
            System.out.println("unhandled:" + message);
        }
    }
}
