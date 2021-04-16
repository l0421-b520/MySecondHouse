package com.ln.test.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @作者: 李跃辉
 * @时间: 2021/4/14 8:50
 */
@Data
public class Employee implements Serializable {

    /**
     * 和solr域名做一个映射
     */
    @Field("id")
    private String id;
    @Field("emp_ename")
    private String ename;
    @Field("emp_eage")
    private Integer eage;
    @Field("emp_title")
    private String title;
    @Field("emp_money")
    private Double money;
    @Field("emp_infor")
    private String  infor;

    public Employee(String id, String ename, Integer eage, String title, Double money, String infor) {
        this.id = id;
        this.ename = ename;
        this.eage = eage;
        this.title = title;
        this.money = money;
        this.infor = infor;
    }

    public Employee() {

    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", ename='" + ename + '\'' +
                ", eage=" + eage +
                ", title='" + title + '\'' +
                ", money=" + money +
                ", infor='" + infor + '\'' +
                '}';
    }
}
