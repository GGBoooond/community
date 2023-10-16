package com.nowcoder.community.entity;

import org.springframework.stereotype.Component;

/**
 * @author wxx
 * @version 1.0
 * 封装分页的相关信息
 */
public class Page {
    //当前页码
    private int current=1;
    //显示上限（用来计算总页码）
    private int limit=10;
    //总页数
    private int rows;
    //查询路径（用来复用分页路径）
    private String path;
    //获取总页数
    public int getPagesNum(){
        return (rows / limit)!=0 ? (rows/limit)+1 :0;
    }
    //获取当前页码
    public int getCurrent() {
        return current;
    }
    //设置当前页码
    public void setCurrent(int current) {
        if(current>=1 ) {
            this.current = current;
        }
    }
    //获取显示的起始行
    public int getOffset(){
        return current<1 ? 0 :(current-1)*limit ;
    }
    //获取下标栏页面显示的最左边
    public int getLeft(){
        return current<=2 ? 1 :current-2;
    }
    //获取下标栏页面显示的最右边
    public int getRight(){
        return current>=getPagesNum()-2 ?getPagesNum() :current+2;
    }
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=0 && limit<=rows) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
