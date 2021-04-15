package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;
import akka.util.Timeout;
import com.lp.akka.notes.messages.GetRequest;
import scala.PartialFunction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

/**
 * 使用Ask方式和其他Actor通信
 * ASK: 向Actor发送一条消息,返回一个Future。当Actor返回响应时,会完成Future。不会向消息发送者的邮箱返回任何消息。
 * @author lipan
 */
public class AskDemoArticleParser extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection articleParseActor;
    private final Timeout timeout;

    /**
     * actorSelection 查询得到这几个 Actor 的引用。
     * @param cacheActorPath 缓存Actor的路径
     * @param httpClientActorPath http Actor的路径
     * @param articleParseActor 文章解析Actor的路径
     * @param timeout 超时时间
     */
    public AskDemoArticleParser(String cacheActorPath, String httpClientActorPath, String articleParseActor, Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor = context().actorSelection(httpClientActorPath);
        this.articleParseActor = context().actorSelection(articleParseActor);
        this.timeout = timeout;
    }

    /**
     * Note there are 3 asks so this potentially creates 6 extra objects:
     * - 3 Promises
     * - 3 Extra actors
     * It's a bit simpler than the tell example.
     */
    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.
                match(ParseArticle.class, msg -> {
                    // 接受到消息后从缓存中获取文章
                    final CompletionStage cacheResult = toJava(ask(cacheActor, new GetRequest(msg.url), timeout));
                    // thenCompose链式异步操作,scala中使用flatMap
                    final CompletionStage result = cacheResult.handle((x, t) -> {
                        return (x != null)
                                ? CompletableFuture.completedFuture(x)
                                : toJava(ask(httpClientActor, msg.url, timeout)). // 向作为HTTP客户端的Actor发送ask请求，请求获取原始文章
                                thenCompose(rawArticle -> toJava( // 向用于文章解析的Actor发送ask请求，请求对原始文章进行解析。
                                                ask(articleParseActor,
                                                        new ParseHtmlArticle(msg.url,
                                                                ((HttpResponse) rawArticle).body), timeout))
                                );
                    }).thenCompose(x -> x);

                    final ActorRef senderRef = sender();
                    result.handle((x,t) -> {
                        if(x != null){
                            if(x instanceof ArticleBody){
                                String body = ((ArticleBody) x).body; //parsed article
                                cacheActor.tell(body, self()); //cache it
                                senderRef.tell(body, self()); //reply
                            } else if(x instanceof String) //cached article
                                senderRef.tell(x, self());
                        } else if( x == null )
                            senderRef.tell(new akka.actor.Status.Failure((Throwable)t), self());
                        return null;
                    });

                }).build();
    }
}
