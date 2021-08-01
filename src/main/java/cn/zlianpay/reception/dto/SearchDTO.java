package cn.zlianpay.reception.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchDTO {

    private String id;
    private String andIncrement;
    private String member;
    private String createTime;
    private String payType;
    private String status;
    private String money;

}
