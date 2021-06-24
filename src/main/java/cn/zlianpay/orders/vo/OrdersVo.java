package cn.zlianpay.orders.vo;

import cn.zlianpay.carmi.entity.Cards;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class OrdersVo {

    /**
     *
     */
    private Integer id;

    /**
     * 订单号
     */
    private String member;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 买家联系方式
     */
    private String contact;

    /**
     * 买家ip
     */
    private String ip;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 订单数量
     */
    @Autowired
    private Integer number;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 支付用户的id（如果有）
     */
    private Integer guestId;

    /**
     * 购买设备
     */
    private String device;

    /**
     * 流水号
     */
    private String payNo;

    /**
     * 付款金额
     */
    private BigDecimal money;

    /**
     * 提交金额
     */
    private BigDecimal price;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 云端id
     */
    private String cloudPayid;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 卡密列表
     */
    private List<Cards> cardInfo;


    /**
     * 标记为使用优惠券
     */
    private Integer isCoupon;

    /**
     * 优惠券id
     */
    private Integer couponId;
}
