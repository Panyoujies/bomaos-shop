package cn.zlianpay.orders.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class OrderVos {

    /**
     * id
     */
    private Integer id;

    /**
     * 订单号
     */
    private String member;

    /**
     * 状态
     */
    private String status;

    /**
     * 支付时间
     */
    private Date payTime;

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
     * 邮件通知
     */
    private String email;

    /**
     * 索引
     */
    private Integer andIncrement;
}
