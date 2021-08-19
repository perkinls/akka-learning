package com.lp.akka.notes.cluster.wordcountv1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author li.pan
 * @title 启动前端程序
 * 使用传统的方式通过前端转发+后端的方式实现单词统计
 */
public class RunFrontServiceApp {
    public static void main(String[] args) {
//        String port = args[0];
//        String directory = args[1];
        String port = "2550";
        String directory = "/Users/lipan/workspace/akka-local/akka-learning/chapter07/src/main/resources/wordcount";
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                //通过akka.cluster.roles节点设置为wordFrontend,便于后端识别
                .withFallback(ConfigFactory.parseString("akka.cluster.roles=[wordFrontend]"))
                .withFallback(ConfigFactory.load("cluster-word-count-v1.conf"));

        ActorSystem system = ActorSystem.create("sys", config);
        ActorRef ref = system.actorOf(Props.create(WordFrontService.class), "wordFrontService");


        // 判断前端服务是否已经准备就绪
        String result = "";
        while (true) {
            Future<Object> fu = Patterns.ask(ref, "isReady", 1000);
            try {
                result = (String) Await.result(fu, Duration.create(1000, "seconds"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if ("ready".equals(result)) {
                System.out.println("==================ready==================");
                break;
            }
        }

        // 文件夹下文本处理成List<Article>
        List<Article> arts = new ArrayList<>();
        File dir = new File(directory);
        File[] files = dir.listFiles();
        try {
            for (File file : files) {
                StringBuilder contentBf = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    contentBf.append(line);
                    line = reader.readLine();
                }
                reader.close();
                arts.add(new Article(file.getName(), contentBf.toString()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 调用前端服务到后端服务 处理单词统计
        Timeout timeout = new Timeout(Duration.create(3, TimeUnit.SECONDS));
        for (Article art : arts) {
            Patterns.ask(ref, art, timeout).onSuccess(new OnSuccess<Object>() {
                @Override
                public void onSuccess(Object result) throws Throwable {
                    CountResult cr = (CountResult)result;
                    System.out.println("文件 " + cr.getId() + "， 单词数：" + cr.getCount());
                }
            }, system.dispatcher());
        }
    }
}
