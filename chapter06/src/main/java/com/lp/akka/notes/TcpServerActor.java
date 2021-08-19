package com.lp.akka.notes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.net.InetSocketAddress;

/**
 * @author li.pan
 * @title Tcp Server端(未深入)
 */
public class TcpServerActor extends UntypedActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        // 获取管理器Actor即TcpManager
        ActorRef tcpManager = Tcp.get(getContext().system()).manager();
        /*
         *Bind指定绑定地址和端口,创建一个TCP服务
         */
        tcpManager.tell(TcpMessage.bind(getSelf(),
                new InetSocketAddress("127.0.0.1", 1234), 100),
                getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // actor发送bind指令,待绑定成功收到Bound
        if (message instanceof Tcp.Bound) {
            Tcp.Bound bound = (Tcp.Bound) message;
            System.out.println("bound: " + bound);
        } else if (message instanceof Tcp.Connected) { // Connected指令表示可以相关通信
            Tcp.Connected conn = (Tcp.Connected)message;
            System.out.println("conn: " + conn);
            ActorRef handler = getContext().actorOf(Props.create(ServerHandler.class));
            getSender().tell(TcpMessage.register(handler), getSelf());
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        ActorRef server = system.actorOf(Props.create(TcpServerActor.class), "server");
    }
}


/**
 * 消息处理的handler
 */
class ServerHandler extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Tcp.Received) {
            Tcp.Received re = (Tcp.Received)message;
            ByteString b = re.data();
            String content = b.utf8String();
            System.out.println("Server: " + content);
            ActorRef conn = getSender();
            conn.tell(TcpMessage.write(ByteString.fromString("Thanks")), getSelf());
        } else if (message instanceof Tcp.ConnectionClosed) {
            System.out.println("Connection is closed " + message);
            getContext().stop(getSelf());
        } else {
            System.out.println("Other Tcp Server: " + message);
        }
    }
}