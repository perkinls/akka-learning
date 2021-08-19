package com.lp.akka.notes;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;
/**
 * @author li.pan
 * @title 事件流是ActorSystem上下文环境中的总线
 */
public class EventStreamActor {

    public static void main(String[] args) {
        Function<Throwable, Supervision.Directive> decider = err -> {
            if (err instanceof ArrayIndexOutOfBoundsException) {
                return Supervision.resume();
            } else {
                return Supervision.stop();
            }
        };

        ActorSystem system = ActorSystem.create("sys");
        Materializer materializer = ActorMaterializer.create(ActorMaterializerSettings.create(system).withSupervisionStrategy(decider), system);
//        Source<Integer, NotUsed> source = Source.range(1, 5);
//        Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(System.out::println);
//        RunnableGraph<NotUsed> graph = source.to(sink);
//        graph.run(materializer);

        Flow<ByteString, String, NotUsed> flowToString = Framing.delimiter(ByteString.fromString("\r\n"), 100).map(x -> x.utf8String());

        Flow<String, AccessLog, NotUsed> flowToAccess = Flow.of(String.class).map(x -> {
            String[] datas = x.split(" ");
            String ip = datas[0];
            String time = datas[1];
            String method = datas[2];
            String resource = datas[3];
            String state = datas[4];
            AccessLog access = new AccessLog(ip, time, method, resource, state);
            return access;
        });

        Flow<AccessLog, ByteString, NotUsed> flowToByte = Flow.of(AccessLog.class).map(x -> ByteString.fromString(x.toString() + "\r\n"));

        Flow<AccessLog, AccessLog, NotUsed> filter404 = Flow.of(AccessLog.class).filter(x -> x.getState().equals("404"));

        Source<ByteString, CompletionStage<IOResult>> source = FileIO.fromPath(Paths.get("access_log.txt"));
        Sink<ByteString, CompletionStage<IOResult>> sink = FileIO.toPath(Paths.get("demo_out.txt"));
        RunnableGraph<CompletionStage<IOResult>> graph = source.via(flowToString).via(flowToAccess).via(filter404).via(flowToByte).to(sink);
        CompletionStage<IOResult> coms = graph.run(materializer);
        coms.thenAccept(System.out::println);
    }
}


class AccessLog {
    private String ip;
    private String time;
    private String method;
    private String resource;
    private String state;

    public AccessLog(String ip, String time, String method, String resource, String state) {
        this.ip = ip;
        this.time = time;
        this.method = method;
        this.resource = resource;
        this.state = state;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
