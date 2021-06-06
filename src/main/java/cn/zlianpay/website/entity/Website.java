package cn.zlianpay.website.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * 网站设置
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
@TableName("sys_website")
public class Website implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站域名
     */
    private String websiteUrl;

    /**
     * 网站logo
     */
    private String websiteLogo;

    /**
     * 商店详情
     */
    private String storeDetails;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 备案ICP
     */
    private String beianIcp;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 网站描述
     */
    private String description;

    /**
     * favicon
     */
    private String favicon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getWebsiteLogo() {
        return websiteLogo;
    }

    public void setWebsiteLogo(String websiteLogo) {
        this.websiteLogo = websiteLogo;
    }

    public String getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(String storeDetails) {
        this.storeDetails = storeDetails;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBeianIcp() {
        return beianIcp;
    }

    public void setBeianIcp(String beianIcp) {
        this.beianIcp = beianIcp;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    @Override
    public String toString() {
        return "Website{" +
        ", id=" + id +
        ", websiteName=" + websiteName +
        ", websiteUrl=" + websiteUrl +
        ", websiteLogo=" + websiteLogo +
        ", storeDetails=" + storeDetails +
        ", contact=" + contact +
        ", beianIcp=" + beianIcp +
        ", keywords=" + keywords +
        ", description=" + description +
        ", favicon=" + favicon +
        "}";
    }

}
