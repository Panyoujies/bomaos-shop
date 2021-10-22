package cn.zlianpay.carmi.utils;

import lombok.Data;
import lombok.ToString;

/**
 * excel 实体类
 * Created by Panyoujie on 2021-10-23 03:01:15
 */
@ToString
@Data
public class ExcelPOJO {

    @excelRescoure(value = "商品名称")
    private String name;

    @excelRescoure(value = "卡密信息")
    private String code;

    @excelRescoure(value = "导出时间")
    private String time;

}
