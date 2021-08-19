package com.lp.akka.notes.cluster.wordcountv3;

import java.io.Serializable;

/**
 * @author li.pan
 * @title 客户程序与集群之间的请求实体
 */
public class Article implements Serializable {
    private String id;
    private String content;

    public Article(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
