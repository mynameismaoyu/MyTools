package com.maoyu.bean.util;

import java.util.Date;
import java.util.List;

/**
 * @author maoyu [2018-10-05 12:20]
 **/
public class Teacher {

    private String name;

    private Integer age;

    private Long account;

    private Date birth;

    private List<Student> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", account=" + account +
                ", birth=" + birth +
                ", list=" + list +
                '}';
    }
}
