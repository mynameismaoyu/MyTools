package com.maoyu.bean.common;

import com.maoyu.bean.common.Student;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author maoyu [2018-10-05 12:20]
 **/
public class Teacher {

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    private Integer age;

    private Long account;

    private Date birth;

    @NotNull(message = "学生对象不能为空")
    private Student student;

    @NotEmpty(message = "集合不能为空")
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", account=" + account +
                ", birth=" + birth +
                ", student=" + student +
                ", list=" + list +
                '}';
    }
}
