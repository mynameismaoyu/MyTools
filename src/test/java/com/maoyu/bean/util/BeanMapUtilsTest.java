package com.maoyu.bean.util;

import com.maoyu.bean.common.Student;
import com.maoyu.bean.common.Teacher;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @author maoyu [2018-10-05 13:50]
 **/
public class BeanMapUtilsTest {

    @Test
    public void testOne() {
        Teacher teacher = new Teacher();
        teacher.setName("zhang san");
        teacher.setAge(45);
        teacher.setBirth(new Date());
        teacher.setAccount(1122334455L);
        Map<String, Object> one = BeanMapUtils.beanToMap(teacher);

        System.out.println("结果一：" + one);
    }

    @Test
    public void testTwo() {
        Teacher teacher = new Teacher();
        teacher.setName("zhang san");
        teacher.setAge(45);
        teacher.setBirth(new Date());
        teacher.setAccount(1122334455L);

        Student student = new Student();
        student.setName("xiao ming");
        student.setAge(15);
        student.setNumber(3001L);
        ArrayList<Student> list = new ArrayList<>();
        list.add(student);
        teacher.setList(list);
        Map<String, Object> two = BeanMapUtils.beanToMap(teacher);
        System.out.println("结果二：" + two);
    }

}
