package cn.zlianpay.reception.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDTO {
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品金额
     */
    private String price;

    /**
     * 首页截图
     */
    private String indexLogo;

    /**
     * 卡密数量
     */
    private Integer cardMember;

    /**
     * 出售卡密数量
     */
    private Integer sellCardMember;

    /**
     * 优惠券
     */
    private Integer isCoupon;

    /**
     * 限制购买
     */
    private Integer restricts;

    /**
     * 批发功能
     */
    private Integer isWholesale;

    /**
     * 发货类型（0-自动，1-手动）
     */
    private Integer shipType;

}
