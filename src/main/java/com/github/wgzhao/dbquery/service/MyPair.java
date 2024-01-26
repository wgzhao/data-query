package com.github.wgzhao.dbquery.service;

/**
 * implement a generic Pair
 * jdk docker does not include javafx package
 */
public class MyPair<U, V>
{
    private U first;
    private V second;

    public MyPair(U first, V second) {

        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
