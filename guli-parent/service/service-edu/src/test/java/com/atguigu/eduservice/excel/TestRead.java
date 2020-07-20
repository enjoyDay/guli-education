package com.atguigu.eduservice.excel;

import com.alibaba.excel.EasyExcel;

/**
 * @author liukun
 * @description
 * @since 2020/6/19
 */
public class TestRead {
    public static void main(String[] args) throws Exception {

        // 写法1：
        String fileName = "D:\\MySourceCode\\guli-education\\easy-excel\\test.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ReadData.class, new ExcelListener()).sheet().doRead();
    }
}
