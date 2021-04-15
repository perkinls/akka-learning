//package com.lp.akka.notes;
//
//import scala.util.Try;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Future;
//import java.util.stream.Collectors;
//
///**
// * @author li.pan
// * @version 1.0.0
// * @Description TODO
// * @createTime 2021年01月14日 13:24:00
// */
//public class FutureConcurrentTest {
//
//    ArrayList<String> articleList = new ArrayList<String>();
//    List<ComposableFuture<String>> futures =
//            articleList
//                    .stream()
//                    .map(article ->
//                            CompletableFuture.supplyAsync(
//                                    () -> ArticleParser.apply(article))
//                    )
//                    .collect(Collectors.toList());
//    Future<List<String>> articlesFuture = com.jasongoodwin.monads.Futures.sequence(futures).get();
//}
