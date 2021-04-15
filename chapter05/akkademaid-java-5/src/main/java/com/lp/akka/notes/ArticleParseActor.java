package com.lp.akka.notes;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年01月14日 12:59:00
 */
public class ArticleParseActor extends AbstractActor {

    private ArticleParseActor() {
        receive(ReceiveBuilder.
                match(ParseArticle.class, x ->{
                            ArticleParser.apply(x.htmlBody).
                                    onSuccess(body -> sender().tell(body, self())).
                                    onFailure(t -> sender().tell(new Status.Failure(t), self()));
                        }
                ).
                build());
    }
}