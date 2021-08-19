package com.lp.akka.notes;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.dispatch.Futures;
import akka.dispatch.OnSuccess;
import akka.japi.Creator;
import akka.japi.Option;
import scala.concurrent.Future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author li.pan
 * @title TypeActor使用方式接近于OOP，通过接口-实现类和函数调用的方式来驱动实现
 */
public class TypeActorOop implements UserService {

    private static Map<String, String> map = new ConcurrentHashMap<>();

    @Override
    public void saveUser(String id, String user) {
        map.put(id, user);
    }

    @Override
    public Future<String> findUserForFuture(String id) {
        return Futures.successful(map.get(id));
    }

    @Override
    public Option<String> findUserForOpt(String id) {
        return Option.some(map.get(id));
    }

    @Override
    public String findUser(String id) {
        return map.get(id);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        // 得到TypeActor扩展对象
        TypedActorExtension typedActorExtension = TypedActor.get(system);

        // 得到接口代理实现
//        UserService userService = typedActorExtension.typedActorOf(
//                new TypedProps<>(UserService.class, TypeActorOop.class));

        UserService userService = typedActorExtension.typedActorOf(
                new TypedProps<>(UserService.class, new Creator<UserService>() {
                    @Override
                    public UserService create() throws Exception, Exception {
                        // 可以实现调用有参构造方法
                        return new TypeActorOop();
                    }
                }));
        System.out.println("userService: " + userService);
        userService.saveUser("1", "afei");

        // 异步执行
        Future<String> fu = userService.findUserForFuture("1");
        fu.onSuccess(new OnSuccess<String>() {
            @Override
            public void onSuccess(String result) throws Throwable, Throwable {
                System.out.println("The future user is : " + result);
            }
        }, system.dispatcher());

        // 阻塞直到有返回值
        Option<String> opt = userService.findUserForOpt("1");
        System.out.println("The Opt user is: " + opt.getClass());

        // 阻塞直到有返回值
        String user = userService.findUser("1");
        System.out.println("The user is : " + user);
    }
}



interface UserService {
    void saveUser(String id, String user);

    Future<String> findUserForFuture(String id);

    Option<String> findUserForOpt(String id);

    String findUser(String id);
}