package com.lp.akka.notes;

import java.io.Serializable;

/**
 * @author lipan
 */
public class ParseArticle implements Serializable {
    public final String htmlBody;
    public ParseArticle(String url) {
        this.htmlBody = url;
    }
}
