package me.xujichang.basic.wrapper;

import java.util.List;

/**
 * Created by xjc on 2017/9/28.
 */

public class WrapperEntityList<T> {
    private int page;
    private int size;
    private int total;
    private List<T> list;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
