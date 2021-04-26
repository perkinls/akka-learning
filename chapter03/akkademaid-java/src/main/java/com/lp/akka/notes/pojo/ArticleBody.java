package com.lp.akka.notes.pojo;

/**
 * 文章内容实体类
 * @author lipan
 */
public class ArticleBody {
    public final String uri;
    public final String body;
    public ArticleBody(String uri, String body) {
        this.uri = uri;
        this.body = body;
    }
}
