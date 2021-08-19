package com.lp.akka.notes;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import com.typesafe.config.Config;

/**
 * @author li.pan
 * @title 实现某个ActorSystem需要和外界做RPC调用，而远程服务端地址和端口可以配置
 */

public class RpcExtensionActor {

    public static void main(String[] args) {
        /*
         * 当调用get方法时,RPCExtProvider.createExtension会自动调用一次.
         * 如果该类配置在akka.extensions里,会在ActorSystem创建时调用一次.
         */
        // RPCExtension rpcExt = RPCExtProvider.getInstance().get(system);
        // rpcExt.rpcCall("hello");

    }
}


/**
 * step1: 实现Extension接口
 * ->包含rpc 端口和服务地址，加载时会传入Extension对象
 */
class RPCExtension implements Extension {
    private String server;
    private int port;

    public RPCExtension(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void rpcCall(String cmd) {
        System.out.println("call " + cmd + "-->" + server + ":" + port);
    }
}

/**
 * step2: 定义ExtensionId,使对象能被Actor-System查找并绑定
 */
class RPCExtProvider extends AbstractExtensionId<RPCExtension> {
    private static RPCExtProvider provider = new RPCExtProvider();

    @Override
    public RPCExtension createExtension(ExtendedActorSystem system) {
        Config config = system.settings().config();
        String server = config.getString("akkademo.server");
        int port = config.getInt("akkademo.port");
        return new RPCExtension(server, port);
    }

    public static RPCExtProvider getInstance() {
        return provider;
    }
}