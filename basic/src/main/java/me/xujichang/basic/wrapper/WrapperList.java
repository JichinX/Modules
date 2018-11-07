package me.xujichang.basic.wrapper;

import java.util.List;

/**
 * Created by xjc on 2017/10/9.
 */

public class WrapperList<T> {
    private int     total;
    private int     page;
    private int     size;
    private List<T> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isOK() {
        return null != list;
    }
}
