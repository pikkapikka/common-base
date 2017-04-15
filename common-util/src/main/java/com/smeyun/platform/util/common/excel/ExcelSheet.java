/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ExcelUtil.java
 * Author:   qxf
 * Date:     2016年12月5日 上午10:59:37
 */
package com.smeyun.platform.util.common.excel;

import java.util.Collection;

/**
 * 用于汇出多个sheet的Vo The <code>ExcelSheet</code>
 * 
 * @author qxf
 * @version 1.0
 */
public class ExcelSheet<T> {
    private String sheetName;
    private String[] headers;
    private Collection<T> dataset;

    /**
     * @return the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * Excel页签名称
     * 
     * @param sheetName
     *            the sheetName to set
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * Excel表头
     * 
     * @return the headers
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * @param headers
     *            the headers to set
     */
    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    /**
     * Excel数据集合
     * 
     * @return the dataset
     */
    public Collection<T> getDataset() {
        return dataset;
    }

    /**
     * @param dataset
     *            the dataset to set
     */
    public void setDataset(Collection<T> dataset) {
        this.dataset = dataset;
    }

}
