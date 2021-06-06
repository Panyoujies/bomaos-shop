package cn.zlianpay.settings.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class PaysVo {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付类型名称
     */
    private String name;

    /**
     * 支付驱动类型
     */
    private String driver;

    /**
     * id
     */
    private String appid;

    /**
     * 密钥
     */
    private String key;

    /**
     * 支付宝官方密钥
     */
    private String mpKey;

    /**
     * 通知地址
     */
    private String createUrl;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 说明
     */
    private String comment;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}
