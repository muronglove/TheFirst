package com.example.administrator.thefirst.Classification;

/**
 * Created by ZZh on 2018/3/15.
 */

public class Classification {
    private String name;
    public Object onItemClickListener;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOnItemClickListener(ClassAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
