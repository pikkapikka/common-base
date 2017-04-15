/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: ExcelUtil.java
 * Author:   qxf
 * Date:     2016年12月5日 上午10:59:37
 */
package com.smeyun.platform.util.common.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smeyun.platform.util.common.errorcode.ErrorCodeConstant;
import com.smeyun.platform.util.common.exception.SmeyunUncheckedException;

/**
 * excel工具类。</br>
 * 支持:Excel 2003/2007/2010</br>
 * 
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class ExcelUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    
    private ExcelUtil()
    {
        
    }
    
    /**
     * @Title: readeExcel
     * 读取excel文件
     * 
     * @param filePath excel文件路径
     * @return Map<String, String[][]> sheet名称及其对应的值
     */
    public static Map<String, String[][]> readeExcel(String filePath)
    {
        LOGGER.debug("begin to read excel file, filePath: ({}).", filePath);
        
        try
        {
            Map<String, String[][]> retData = new HashMap<String, String[][]>();
            
            //文件流
            FileInputStream in = new FileInputStream(filePath);
            
            //这种方式 Excel 2003/2007/2010 都是可以处理的
            Workbook workbook = WorkbookFactory.create(in);
            int sheetCount = workbook.getNumberOfSheets();
            
            //遍历sheet
            for (int s = 0; s < sheetCount; s++)
            {
                LOGGER.debug("begin read {} sheet", String.valueOf(s + 1));
                Sheet sheet = workbook.getSheetAt(s);
                String name = sheet.getSheetName();
                int rowCount = sheet.getLastRowNum();
                
                List<List<String>> tempV = new ArrayList<List<String>>();
                
                //遍历行
                for (int r = 0; r <= rowCount; r++)
                {
                    LOGGER.debug("begin read {} row", String.valueOf(r + 1));
                    Row row = sheet.getRow(r);
                    if (null == row)
                    {
                        continue;
                    }
                    int cellCount = row.getLastCellNum();
                    
                    List<String> cellV = new ArrayList<String>();
                    //遍历列
                    for (int c = 0; c < cellCount; c++)
                    {
                        Cell cell = row.getCell(c);
                        //获取cell值(全部处理为String类型)
                        cellV.add(getValue(cell));
                    }
                    
                    tempV.add(cellV);
                }
                
                retData.put(name, convertData(tempV));
            }
            
            return retData;
        }
        catch (InvalidFormatException e)
        {
            LOGGER.error("read excel error, file=" + filePath, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
        catch (IOException e)
        {
            LOGGER.error("read excel error, file=" + filePath, e);
            throw new SmeyunUncheckedException(ErrorCodeConstant.INNER_ERROR, e);
        }
    }
    
    private static String[][] convertData(List<List<String>> tempData)
    {
        int cellMax = getMaxCellSize(tempData);
        int rowMax = tempData.size();
        
        String[][] data = new String[rowMax][cellMax];
        
        for (int rowIdx = 0; rowIdx < rowMax; rowIdx++)
        {
            List<String> datas = tempData.get(rowIdx);
            int tempCellSize = datas.size();
            
            for (int cellIdx = 0; cellIdx < tempCellSize; cellIdx++)
            {
                data[rowIdx][cellIdx] = datas.get(cellIdx);
            }
        }
        
        return data;
    }
    
    private static int getMaxCellSize(List<List<String>> tempData)
    {
        int max = 0;
        for (List<String> list : tempData)
        {
            if (list.size() > max)
            {
                max = list.size();
            }
        }
        
        return max;
    }
    
    /**
     * @Title: getValue
     * @Description: 對每個元素進行格式化
     * @param @param cell
     * @param @return    param
     * @return String    returnType
     * @throws
     */
    @SuppressWarnings("deprecation")
    private static String getValue(Cell cell)
    {
        String cellValue = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        
        if (cell == null)
        {
            return cellValue;
        }
        else
        {
            switch (cell.getCellTypeEnum())
            {
                case STRING://文本 
                    cellValue = cell.getStringCellValue();
                    break;
                case BLANK://空白
                    cellValue = cell.getStringCellValue();
                    break;
                case BOOLEAN://布尔型 
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case NUMERIC://日期或数字
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        cellValue = fmt.format(cell.getDateCellValue());
                    }
                    else
                    {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    
                    break;
                case ERROR://错误
                    cellValue = "ERROR";
                    break;
                case FORMULA://单元格cell为公式时读取值
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                default:
                    break;
            }
            LOGGER.debug("cell value:" + cellValue);
            return cellValue;
        }
    }
    
    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, OutputStream out)
    {
        exportExcel(headers, dataset, out, null);
    }
    
    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("resource")
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern)
    {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        
        write2Sheet(sheet, headers, dataset, pattern);
        try
        {
            workbook.write(out);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
    
    @SuppressWarnings("resource")
    public static void exportExcel(String[][] datalist, OutputStream out)
    {
        try
        {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet();
            
            for (int i = 0; i < datalist.length; i++)
            {
                String[] r = datalist[i];
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < r.length; j++)
                {
                    HSSFCell cell = row.createCell(j);
                    //cell max length 32767
                    if (r[j].length() > 32767)
                    {
                        r[j] = "--此字段过长(超过32767),已被截断--" + r[j];
                        r[j] = r[j].substring(0, 32766);
                    }
                    cell.setCellValue(r[j]);
                }
            }
            //自动列宽
            if (datalist.length > 0)
            {
                int colcount = datalist[0].length;
                for (int i = 0; i < colcount; i++)
                {
                    sheet.autoSizeColumn(i);
                }
            }
            workbook.write(out);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
    
    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param <T>
     * @param sheets {@link ExcelSheet}的集合
     * @param out    与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, OutputStream out)
    {
        exportExcel(sheets, out, null);
    }
    
    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param <T>
     * @param sheets  {@link ExcelSheet}的集合
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("resource")
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, OutputStream out, String pattern)
    {
        if (CollectionUtils.isEmpty(sheets))
        {
            return;
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        for (ExcelSheet<T> sheet : sheets)
        {
            // 生成一个表格
            HSSFSheet hssfSheet = workbook.createSheet(sheet.getSheetName());
            write2Sheet(hssfSheet, sheet.getHeaders(), sheet.getDataset(), pattern);
        }
        try
        {
            workbook.write(out);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
    
    /**
     * 每个sheet的写入
     *
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     * @param pattern 日期格式
     */
    private static <T> void write2Sheet(HSSFSheet sheet, String[] headers, Collection<T> dataset, String pattern)
    {
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++)
        {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext())
        {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            try
            {
                if (t instanceof Map)
                {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) t;
                    int cellNum = 0;
                    for (String k : headers)
                    {
                        if (map.containsKey(k) == false)
                        {
                            LOGGER.error("Map 中 不存在 key [" + k + "]");
                            continue;
                        }
                        Object value = map.get(k);
                        HSSFCell cell = row.createCell(cellNum);
                        cell.setCellValue(String.valueOf(value));
                        cellNum++;
                    }
                }
                else
                {
                    List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                    int cellNum = 0;
                    for (int i = 0; i < fields.size(); i++)
                    {
                        HSSFCell cell = row.createCell(cellNum);
                        Field field = fields.get(i).getField();
                        field.setAccessible(true);
                        Object value = field.get(t);
                        String textValue = null;
                        if (value instanceof Integer)
                        {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        }
                        else if (value instanceof Float)
                        {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        }
                        else if (value instanceof Double)
                        {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        }
                        else if (value instanceof Long)
                        {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        }
                        else if (value instanceof Boolean)
                        {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        }
                        else if (value instanceof Date)
                        {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        }
                        else if (value instanceof String[])
                        {
                            String[] strArr = (String[]) value;
                            for (int j = 0; j < strArr.length; j++)
                            {
                                String str = strArr[j];
                                cell.setCellValue(str);
                                if (j != strArr.length - 1)
                                {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        }
                        else if (value instanceof Double[])
                        {
                            Double[] douArr = (Double[]) value;
                            for (int j = 0; j < douArr.length; j++)
                            {
                                Double val = douArr[j];
                                // 资料不为空则set Value
                                if (val != null)
                                {
                                    cell.setCellValue(val);
                                }
                                
                                if (j != douArr.length - 1)
                                {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        }
                        else
                        {
                            // 其它数据类型都当作字符串简单处理
                            String empty = StringUtils.EMPTY;
                            ExcelCell anno = field.getAnnotation(ExcelCell.class);
                            if (anno != null)
                            {
                                empty = anno.defaultValue();
                            }
                            textValue = value == null ? empty : value.toString();
                        }
                        if (textValue != null)
                        {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                        
                        cellNum++;
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.error("", e);
            }
        }
        // 设定自动宽度
        for (int i = 0; i < headers.length; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * 根据annotation的seq排序后的栏位
     *
     * @param clazz
     * @return
     */
    private static List<FieldForSortting> sortFieldByAnno(Class<?> clazz)
    {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<FieldForSortting> fields = new ArrayList<FieldForSortting>();
        List<FieldForSortting> annoNullFields = new ArrayList<FieldForSortting>();
        for (Field field : fieldsArr)
        {
            ExcelCell ec = field.getAnnotation(ExcelCell.class);
            if (ec == null)
            {
                // 没有ExcelCell Annotation 视为不汇入
                continue;
            }
            int id = ec.index();
            fields.add(new FieldForSortting(field, id));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void sortByProperties(List<? extends Object> list, boolean isNullHigh, boolean isReversed,
            String... props)
    {
        if (CollectionUtils.isNotEmpty(list))
        {
            Comparator<?> typeComp = ComparableComparator.comparableComparator();
            if (isNullHigh == true)
            {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            }
            else
            {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed)
            {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }
            
            List<Object> sortCols = new ArrayList<Object>();
            
            if (props != null)
            {
                for (String prop : props)
                {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0)
            {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }
    
}
