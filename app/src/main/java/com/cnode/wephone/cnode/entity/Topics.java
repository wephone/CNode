package com.cnode.wephone.cnode.entity;

import java.util.List;

/**
 * Created by ASUS on 2016/3/21.
 * 主题列表 应该是bean对象
 */
public class Topics {
    private List<Topic> data;

    public List<Topic> getData() {
        return data;
    }

    public void setData(List<Topic> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Topics{" +
                "data=" + data +
                '}';
    }
}
