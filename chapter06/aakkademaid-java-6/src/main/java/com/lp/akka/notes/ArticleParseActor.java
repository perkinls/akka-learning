package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;

/**
 * @author lipan
 */
public class ArticleParseActor extends AbstractActor {
    private ArticleParseActor() {
        receive(ReceiveBuilder.
                match(String.class, html -> {
                            System.out.println(ArticleParser.apply(html).orElse("empty?"));

                            ArticleParser.apply(html).
                                    onSuccess(body -> sender().tell(body, self())).
                                    onFailure(t -> sender().tell(new Status.Failure(t), self()));
                        }
                ).
                matchAny(x -> {
                    System.out.println("GOT A MSG!!! " + x);
                }).
                build());
    }
}

