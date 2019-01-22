package com.ztj.dictdemo.common.bean;

import lombok.Data;

import java.util.List;

@Data
public class TableData<T> {

    private long total;

    private List<T> rows;

    public TableData() {
        this.total = 0;
    }

    public TableData(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
