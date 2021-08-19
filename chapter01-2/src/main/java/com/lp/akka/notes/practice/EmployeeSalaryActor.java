package com.lp.akka.notes.practice;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Procedure;

/**
 * @author li.pan
 * @title 利用become切换状态计算不同等级员工薪资
 */
public class EmployeeSalaryActor extends UntypedActor {

    Procedure<Object> LEVEL1 = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof String) {
                if (message.equals("end")) {
                    getContext().unbecome();
                }
            } else {
                Employee emp = (Employee) message;
                double result = emp.getSalary() * 1.8;
                System.out.println("员工" + emp.getName() + "的奖金为：" + result);
            }
        }
    };

    Procedure<Object> LEVEL2 = new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if (message instanceof String) {
                if (message.equals("end")) {
                    getContext().unbecome();
                }
            } else {
                Employee emp = (Employee) message;
                double result = emp.getSalary() * 1.5;
                System.out.println("员工" + emp.getName() + "的奖金为：" + result);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
        String level = (String) message;

        if (level.equals("1")) {
            getContext().become(LEVEL1);
        } else if (level.equals("2")) {
            getContext().become(LEVEL2);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        ActorRef ref = system.actorOf(Props.create(EmployeeSalaryActor.class), "simpleActorDemo");
        ref.tell("1", ActorRef.noSender());
        ref.tell(new Employee(10000, "张三"), ActorRef.noSender());
        ref.tell(new Employee(20000, "李四"), ActorRef.noSender());
        ref.tell("end", ActorRef.noSender());
        ref.tell("2", ActorRef.noSender());
        ref.tell(new Employee(10000, "王五"), ActorRef.noSender());
        ref.tell(new Employee(20000, "赵六"), ActorRef.noSender());
    }
}