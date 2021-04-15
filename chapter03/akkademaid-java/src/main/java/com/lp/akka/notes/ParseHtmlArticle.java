package com.lp.akka.notes;

/**
 * @author lipan
 */
public class ParseHtmlArticle {
    public final String uri, htmlString;

    public ParseHtmlArticle(String uri, String htmlString) {
        this.uri = uri;
        this.htmlString = htmlString;
    }
}
