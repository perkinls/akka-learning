package com.lp.akka.notes;

/**
 * @author li.pan
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年01月14日 12:59:00
 */
public class ParseArticle {
    public final String htmlBody;

    public ParseArticle(String url) {
        this.htmlBody = url;
    }
}
