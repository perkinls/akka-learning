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
 * @title Tcp 客户端Actor(未深入)
 */
public class TcpClientActor extends UntypedActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        ActorRef tcp = Tcp.get(getContext().system()).manager();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 1234);
        tcp.tell(TcpMessage.connect(address), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Tcp.Connected) {
            ActorRef conn = getSender();
            ActorRef clientHandler = getContext().actorOf(Props.create(ClientHandler.class), "clientHandler");
            conn.tell(TcpMessage.register(clientHandler), getSelf());
            conn.tell(TcpMessage.write(ByteString.fromString("Hello Akka")), getSelf());
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys2");
        ActorRef server = system.actorOf(Props.create(TcpClientActor.class), "client");
    }
}

class ClientHandler extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Tcp.Received) {
            Tcp.Received re = (Tcp.Received)message;
            ByteString b = re.data();
            String content = b.utf8String();
            System.out.println("Client: " + content);
        } else {
            System.out.println("Other Tcp Server: " + message);
        }
    }
}