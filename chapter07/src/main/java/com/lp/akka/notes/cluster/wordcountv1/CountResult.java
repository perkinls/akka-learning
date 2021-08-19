package com.lp.akka.notes.cluster.wordcountv1;

import java.io.Serializable;

/**
 * @author li.pan
 * @title 集群返回给客户程序的响应实体
 */
public class CountResult implements Serializable {
    private String id;
    private int count;


    public CountResult(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
