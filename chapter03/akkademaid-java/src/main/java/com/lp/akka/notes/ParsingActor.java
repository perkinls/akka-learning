package com.lp.akka.notes;


import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.lp.akka.notes.pojo.ArticleBody;
import com.lp.akka.notes.pojo.ParseHtmlArticle;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import scala.PartialFunction;

/**
 * @author lipan
 */
public class ParsingActor extends AbstractActor {
    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.
                match(ParseHtmlArticle.class, msg -> {
                    String body = ArticleExtractor.INSTANCE.getText(msg.htmlString);
                    sender().tell(new ArticleBody(msg.uri, body), self());
                }).build();
    }
}
