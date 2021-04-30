package com.akkademy;

import com.jasongoodwin.monads.Futures;
import com.lp.akka.notes.ArticleParser;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 使用Futures并行编程测试
 */
public class ReadFilesWithFuturesTest {
    @Test
    public void shouldReadFilesWithFutures() throws Exception {
        List<Integer> list = IntStream.range(0, 2000).boxed().collect(Collectors.toList());
        List futures = (List) list
                .stream()
                .map(x -> CompletableFuture.supplyAsync(() -> ArticleParser.apply(TestHelper.file))) // 异步执行
                .collect(Collectors.toList());

        long start = System.currentTimeMillis();
        // 使用better-java-monads库来处理多个Future，sequence方法可以将一个Future列表转化成包含结果列表的单个Future
        Futures.sequence(futures).get();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("ReadFilesWithFuturesTest Took: " + elapsedTime);
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                        futures.stream().
                                map(future -> future.join()).
                                collect(Collectors.<T>toList())
        );
    }
}

