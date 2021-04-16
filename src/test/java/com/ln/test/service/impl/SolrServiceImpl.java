package com.ln.test.service.impl;

import com.ln.test.entity.Employee;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 李跃辉
 * @时间: 2021/4/13 23:46
 */
public class SolrServiceImpl {

    /**
     * 编写测试
     */
    final String connectstring ="http://localhost:8081/solr/core_demo/";
    HttpSolrClient solrClient= new HttpSolrClient.Builder(connectstring).build();

    @Test
    public void addAndUptdate() throws IOException, SolrServerException {
        Employee employee1 = new Employee("1001","李跃辉",24,"CTO",20000.0,"工作严谨的好青年！");
        Employee employee2 = new Employee("1002","李世民",24,"皇上",100000.0,"拥有无数的女人，用不尽的财富！");
        Employee employee3 = new Employee("1003","李元霸",24,"将军",1000.0,"打天下的时候，骂天被老天爷给劈死了！");
        Employee employee4 = new Employee("1004","李白",24,"诗人",5000.0,"一生就爱喝酒，爱做诗！");
        Employee employee5 = new Employee("1005","李自清",24,"作家",200.0,"台湾作家，代表作《背影》！");
        Employee employee6 = new Employee("1006","李奎",24,"将军",30000.0,"李逵背老娘下山，老娘被老虎吃了！");
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employee4);
        employees.add(employee5);
        employees.add(employee6);
        // UpdateResponse 对象用来做增删改， QueryResponse是用来做查询的
        UpdateResponse updateResponse = solrClient.addBeans(employees);
        // 事务提交
        solrClient.commit();
        solrClient.close();
        System.out.println("添加成功");
    }

    @Test
    public void query() throws IOException, SolrServerException {
        // 创建查询条件
        SolrQuery sq = new SolrQuery();

        // q在solr中代表的是查询 ----set
        sq.set("q","*:*");

        // Filter 是过滤查询  ---add
        sq.addFilterQuery("emp_eage:[20 TO *]");

        // 排序  ---set (我这里设置的根据年龄排序) --针对与solr 5.x 版本之前
        sq.setSort("emp_eage",SolrQuery.ORDER.desc);

        // 分页  ---set 从0开始 到10 结束
        sq.setStart(0);
        sq.setRows(10);

        // 设置高亮 ---set
        sq.setHighlight(true);
        // 添加高亮字段   ---add
        sq.addHighlightField("emp_infor");
        // 设置高亮样式 ---set 设置前缀 后缀
        sq.setHighlightSimplePre("<font color='red'>");
        sq.setHighlightSimplePost("</font>");

        QueryResponse result = solrClient.query(sq);

        // 这里可以从result获得查询数据(两种方式如下)

        // 1.获取document数据
        System.out.println("1.获取document数据-------------------------");
        SolrDocumentList results = result.getResults();
        // 获取查询的条数
        System.out.println("一共查询到" + results.getNumFound() + "条记录");
        for (SolrDocument solrDocument : results) {
            System.out.println("id:" + solrDocument.get("id")+",ename:" + solrDocument.get("emp_ename")+",eage:" + solrDocument.get("emp_eage")+",title:" + solrDocument.get("emp_title")
            +",money:" + solrDocument.get("emp_money")+",info:" + solrDocument.get("emp_infor"));
            System.out.println();

        }

        // 2.获取对象信息,需要传入对应对象的类class
        System.out.println("2.获取对象信息,需要传入对应对象的类class-----------");
        List<Employee> employees = result.getBeans(Employee.class);
        System.out.println("一共查询到" + employees.size() + "条记录");
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        solrClient.commit();
        solrClient.close();
        System.out.println("查询成功！");
    }

    @Test
    public void deleteById() throws IOException, SolrServerException {
        // 根据主建删除索引
        //UpdateResponse updateResponse = solrClient.deleteById("1006");
        // *:* 相当于对solr索引数据库做一次清空
        UpdateResponse updateResponse = solrClient.deleteByQuery("*:*");
        solrClient.commit();
        solrClient.close();
        System.out.println("索引删除成功！");
    }

}


