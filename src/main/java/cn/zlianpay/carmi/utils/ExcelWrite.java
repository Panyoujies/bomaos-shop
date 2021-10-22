package cn.zlianpay.carmi.utils;

import cn.zlianpay.common.core.utils.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel 操作
 * Created by Panyoujie on 2021-10-23 03:01:15
 */
public class ExcelWrite {

    /**
     * 方法一：
     * 实体类数据写入新建的excel
     * @path：excel文件路径
     * @array[]：文件首行数据列名,可为空,为空时不存在首行列名
     * @list<T>:实体类数据数列
     */
    public static <T> String writeToExcelByPOJO(String path, String[] array, List<T> list)  {
       /* for (T t : list) {
            System.out.println(t);
        }*/
        //创建工作薄
        Workbook wb = new XSSFWorkbook();
        /**标题和页码*/
        CellStyle titleStyle = wb.createCellStyle();
        // 设置单元格对齐方式
        titleStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        //titleStyle.setVerticalAlignment(); // 默认垂直居中
        // 设置字体样式
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 12); // 字体高度
        titleFont.setFontName("黑体"); // 字体样式
        titleStyle.setFont(titleFont);
        //创建sheet
        Sheet sheet = wb.createSheet("第一页");
        sheet.autoSizeColumn(0);// 自动设置宽度
        // 在sheet中添加标题行
        Row row = sheet.createRow((int) 0);// 行数从0开始
        for (int i = 0; i < array.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(array[i]);
            cell.setCellStyle(titleStyle);
        }
        /**数据样式*/
        // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
        CellStyle dataStyle = wb.createCellStyle();
        // 设置居中样式
        dataStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中

        /**处理实体类数据并写入*/
        //获取当前的泛型对象
        Object obj = list.get(0);
        ArrayList arrayList = new ArrayList();
        //LinkedHashMap保证顺序
        LinkedHashMap<String, Object> POJOfields = getPOJOFieldAndValue(obj);
        for (int i = 0; i < array.length; i++) {
            for (Map.Entry<String, Object> map : POJOfields.entrySet()) {
                if (map.getKey().equals(array[i])) {
                    arrayList.add(map.getValue());
                }
            }
        }
        if (array.length != arrayList.size()) {
            return "标题列数和实体类标记数不相同";
        }
        try {
            //数据从序号1开始
            int index = 1;
            //利用迭代器，遍历集合数据，产生数据行
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                row = sheet.createRow(index);// 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
                index++;
                T t = (T) it.next();
                //System.out.println("t:" + t);
                for (int i = 0; i < arrayList.size(); i++) {
                    String fieldName = (String) arrayList.get(i);
                    //System.out.println(fieldName);
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    //System.out.println(getMethodName);
                    Class<? extends Object> tCls = t.getClass();// 泛型为Object以及所有Object的子类
                    //System.out.println(tCls);
                    Method method = tCls.getMethod(getMethodName, new Class[]{});// 通过方法名得到对应的方法
                    //PropertyDescriptor pd = new PropertyDescriptor((String) arrayList.get(i), it.getClass());
                    //获取成员变量的get方法
                    //Method method = pd.getWriteMethod();
                    Object value = method.invoke(t, new Object[]{});// 动态调用方,得到属性值
                    //System.out.println(value.toString());
                    Cell cell = row.createCell(i);
                    if (value != null) {
                        if (value instanceof Date) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            value = simpleDateFormat.format(value);
                        }
                        cell.setCellValue(value.toString());// 为当前列赋值
                        cell.setCellStyle(dataStyle);//设置数据的样式
                    }
                }
            }

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            String fileName = "值联云卡-卡密" + DateUtil.subData() + ".xls";

            FileOutputStream fileOut = new FileOutputStream(path + fileName);

            wb.write(fileOut);
            fileOut.flush();
            wb.close();
            fileOut.close();
            return "file/download/excel/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "faile";
    }

    /**
     * 获取对应的实体类成员
     */
    private static LinkedHashMap<String, Object> getPOJOFieldAndValue(Object T) {
        //声明返回结果集
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        Field[] fields = T.getClass().getDeclaredFields();//获取属性名
        if (fields != null) {
            for (Field field : fields) {
                excelRescoure Rescoure = field.getAnnotation(excelRescoure.class);
                if (Rescoure.value() != null && !"".equals(Rescoure.value())) {
                    result.put(Rescoure.value(), field.getName());
                }
            }
        } else {
            return null;
        }
        return result;
    }

}
