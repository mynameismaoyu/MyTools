package com.maoyu.bean.util;

import com.maoyu.bean.common.Student;
import com.maoyu.bean.common.Teacher;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author maoyu [2018-10-06 10:54]
 **/
public class BeanValidatorTest {

    @Test
    public void testOne() {
        Student student = new Student();
        student.setName("xiao hong");
        BeanValidator.validateField(student);
    }

    @Test
    public void testTwo() {
        Teacher teacher = new Teacher();
        teacher.setName("zhao liu");
        teacher.setAge(30);
        teacher.setStudent(new Student());
        teacher.setList(Lists.<Student>newArrayList(new Student()));
        BeanValidator.validateField(teacher);
        System.out.println(teacher);
    }

}
