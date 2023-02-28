package com.graduation_project.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static  <V> V copyBean(Object source, Class<V> clazz) {
        V target = null;
        try {
            target = clazz.newInstance();
            BeanUtils.copyProperties(source,target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    public static <V, O> List<V> copyBeanList(List<O> sourceList, Class<V> clazz) {
           return sourceList.stream()
                    .map(source -> copyBean(source,clazz))
                   .collect(Collectors.toList());
    }
}
