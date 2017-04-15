/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ExcelUtil.java
 * Author:   qxf
 * Date:     2016年12月5日 上午10:59:37
 */
package com.smeyun.platform.util.common.excel;

import java.lang.reflect.Field;

/**
 * The <code>FieldForSortting</code>
 * 
 * @author Quin.Peng
 * @version 1.0, Created at 2016年9月22日
 */
public class FieldForSortting {
    private Field field;
    private int index;

    /**
     * @param field
     */
    public FieldForSortting(Field field) {
        super();
        this.field = field;
    }

    /**
     * @param field
     * @param index
     */
    public FieldForSortting(Field field, int index) {
        super();
        this.field = field;
        this.index = index;
    }

    /**
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * @param field
     *            the field to set
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

}
