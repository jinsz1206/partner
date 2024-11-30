package com.jsz.partner_backend.EasyExcelExample;



import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExampleUser {
    /**
     * id
     */
    @ExcelProperty("成员编号")
    private String planetCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("成员昵称")
    private String username;

}