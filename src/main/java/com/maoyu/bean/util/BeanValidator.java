package com.maoyu.bean.util;

import com.maoyu.bean.common.BaseException;
import com.maoyu.bean.common.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实体字段校验
 *
 * @author maoyu [2018-10-05 21:41]
 **/
public class BeanValidator {

    private BeanValidator() {

    }

    /**
     * 由ValidatorFactory工厂获得validator
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 字段校验,若出现异常直接throw异常
     *
     * @param table
     * @param <T>
     */
    public static <T> void validateField(T table) {
        if (table == null) {
            throw new BaseException("传入的对象为空!");
        } else {
            Result result = getObjectInfo(getConstraintSet(table));
            if (!result.isSuccess()) {
                throw new BaseException(result.getMessage());
            }
        }
    }

    /**
     * 分组字段校验,若出现异常直接throw异常
     *
     * @param table
     * @param groups
     * @param <T>
     */
    public static <T> void validateField(T table, Class... groups) {
        if (table == null) {
            throw new BaseException("传入的对象为空!");
        } else {
            Result result = getObjectInfo(getConstraintSet(table, groups));
            if (!result.isSuccess()) {
                throw new BaseException(result.getMessage());
            }
        }
    }

    private static <T> Result getObjectInfo(Set<ConstraintViolation<T>> validObject) {
        //错误信息集合
        List<String> errorMessageList = new ArrayList<>();
        //判断是否有错误信息,若有错误信息打包Object返回.else返回null
        if (CollectionUtils.isNotEmpty(validObject)) {
            for (ConstraintViolation<T> constraintViolation : validObject) {
                if (StringUtils.isNotBlank(constraintViolation.getMessage())) {
                    errorMessageList.add(constraintViolation.getMessage());
                } else {
                    errorMessageList.add("输入不合法");
                }
            }
            // jdk 1.8的写法
            // String errorMessage = errorMessageList.stream().collect(Collectors.joining("！", "[", "]"));
            // jdk 1.7的写法
            StringBuilder joiner = new StringBuilder();
            joiner.append("[");
            for (String s : errorMessageList) {
                joiner.append(s);
                joiner.append("!");
            }
            joiner.append("]");
            String errorMessage = joiner.toString();

            return Result.buildFailure(errorMessage);
        } else {
            return Result.buildSuccess("");
        }
    }

    /**
     * 得到Set集合
     *
     * @param table 实体对象
     * @param <T>   实体对象类型
     * @return 错误信息Set集合
     */
    private static <T> Set<ConstraintViolation<T>> getConstraintSet(T table) {
        return validator.validate(table);
    }

    /**
     * 重载,有分组情况
     *
     * @param table 实体对象
     * @param temp  分组信息
     * @param <T>   实体对象类型
     * @return 错误信息Set集合
     */
    private static <T> Set<ConstraintViolation<T>> getConstraintSet(T table, Class... temp) {
        return validator.validate(table, temp);
    }
}
