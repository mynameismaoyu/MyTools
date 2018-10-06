package com.maoyu.bean.util;

import com.github.pagehelper.Page;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean<->Map工具类
 * 利用BeanMap可高效进行对象与Map的相互转换
 *
 * @author maoyu [2018-10-05 10:38]
 **/
public class BeanMapUtils {

    private BeanMapUtils() {

    }

    /**
     * 将对象转换为map
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map转换为javabean对象
     *
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> objList) {
        //兼容PageHelper
        List<Map<String, Object>> list;
        if (objList instanceof Page) {
            Page page = (Page) objList;
            list = new Page<>();
            //实际上前台只需要total和data
            ((Page<Map<String, Object>>) list).setPageNum(page.getPageNum());
            ((Page<Map<String, Object>>) list).setPageSize(page.getPageSize());
            ((Page<Map<String, Object>>) list).setStartRow(page.getStartRow());
            ((Page<Map<String, Object>>) list).setEndRow(page.getEndRow());
            ((Page<Map<String, Object>>) list).setTotal(page.getTotal());
            ((Page<Map<String, Object>>) list).setPages(page.getPages());
            ((Page<Map<String, Object>>) list).setReasonable(page.getReasonable());
            ((Page<Map<String, Object>>) list).setPageSizeZero(page.getPageSizeZero());
        } else {
            list = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(objList)) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(maps)) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }

}
