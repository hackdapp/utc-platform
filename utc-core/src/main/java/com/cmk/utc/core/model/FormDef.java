/*
 * 文件名：FormDef.java
 * 版权：Copyright by www.wmccn.com
 * 描述：
 * 修改人：LIFE2014
 * 修改时间：2014-10-30
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.cmk.utc.core.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.cmk.utc.core.annotation.FieldMeta;


/**
 * 表单定义,用于组织界面字段及界面操作
 * @author zhangliang
 * @version 2014-10-30
 * @see FormDef
 * @since
 */
public class FormDef {
    @FieldMeta(comment="",fieldName="formCode",fieldLength=12)
    public String code;
    private String name;
    private String table;
    private String view;
    private String datasource;
    
    private List<FormItem> itemList = new ArrayList<FormItem>();
    
    
    public static void main(String[] args) {
        for (Field tmp : FormDef.class.getFields()) {
            System.out.println(tmp.getName()+"//"+tmp.getAnnotation(FieldMeta.class).fieldName());
        }
    }
}
