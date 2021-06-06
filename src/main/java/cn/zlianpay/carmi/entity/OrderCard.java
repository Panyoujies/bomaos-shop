package cn.zlianpay.carmi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import java.io.Serializable;

/**
 * 订单关联卡密表
 * Created by Panyoujie on 2021-03-29 22:27:41
 */
@TableName("sys_order_card")
public class OrderCard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer cardId;

    /**
     * 
     */
    private Integer orderId;

    /**
     * 创建时间
     */
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderCard{" +
        ", id=" + id +
        ", cardId=" + cardId +
        ", orderId=" + orderId +
        ", createdAt=" + createdAt +
        "}";
    }

}
