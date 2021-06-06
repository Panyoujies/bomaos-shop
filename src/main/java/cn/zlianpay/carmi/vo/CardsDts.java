package cn.zlianpay.carmi.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CardsDts {

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 卡密信息
     */
    private String cardInfo;

    /**
     * 过滤重复
     */
    private Integer repeat;

}
