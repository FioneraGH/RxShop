package com.centling.entity;

/**
 * BaseEntity
 * Created by fionera on 17-2-14 in sweeping_robot.
 */

public class BaseEntity<T> {
    public int statusCode;
    public String statusMsg;
    public T result;

    @Override
    public String toString() {
        return "BaseEntity{" + "resCode='" + statusCode + '\'' + ", resMsg='" + statusMsg + '\''
                + ", " + "data=" + result + '}';
    }
}
