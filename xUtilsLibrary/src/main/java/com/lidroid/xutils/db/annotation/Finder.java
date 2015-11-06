package com.lidroid.xutils.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wyouflf
 * Date: 13-9-10
 * Time: 下午6:44
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//A B对象有1对多关系，分别存在a,b表
// 另外A对象存DB的时候，并自动不会存B@finder，需要自己存噢！！！L
public @interface Finder {
    //b表中需要有一列特定标识，来关联a表中的某个A对象，这个valueColumn就是A在a表中的一个列的名字，通常为主键
    //在查找A对象的时候，需要在A中获取在该列的值，用于在b表中查找多个关系的B对象
    String valueColumn();

    //b表中需要有一列特定标识，来关联a表中的某个A对象，而targetColumn就是A的标识在a表中的列在b表中该列的名字
    String targetColumn();
}
