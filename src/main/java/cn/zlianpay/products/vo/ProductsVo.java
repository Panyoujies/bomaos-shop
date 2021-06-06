package cn.zlianpay.products.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class ProductsVo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品名称
     */
    private String classifyName;

    /**
     * 卡密数量
     */
    private Integer cardMember;

    /**
     * 出售卡密数量
     */
    private Integer sellCardMember;

    /**
     * 商品金额
     */
    private String price;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 商品链接
     */
    private String link;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 商品详情
     */
    private String pdInfo;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 删除时间
     */
    private Date deletedAt;

    /**
     * 分类ID
     */
    private Integer classifyId;

    /**
     * 商品logo
     */
    private String imageLogo;
}
