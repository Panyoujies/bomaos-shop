package cn.zlianpay.reception.common;

/**
 * 支付接口枚举类
 * Created by Panyoujie on 2021-10-23 00:11:08
 */
public enum PaysEnmu {
    MQPAY_ALIPAY("mqpay_alipay"),
    MQPAY_WXPAY("mqpay_wxpay"),
    ZLIANPAY_ALIPAY("zlianpay_alipay"),
    ZLIANPAY_WXPAY("zlianpay_wxpay"),
    ZLIANPAY_QQPAY("zlianpay_qqpay"),
    YUNGOUOS_WXPAY("yungouos_wxpay"),
    YUNGOUOS_ALIPAY("yungouos_alipay"),
    XUNHUPAY_WXPAY("xunhupay_wxpay"),
    XUNHUPAY_ALIPAY("xunhupay_alipay"),
    JIEPAY_WXPAY("jiepay_wxpay"),
    JIEPAY_ALIPAY("jiepay_alipay"),
    PAYJS_WXPAY("payjs_wxpay"),
    PAYJS_ALIPAY("payjs_alipay"),
    WXPAY("wxpay"),
    ALIPAY("alipay"),
    ALIPAY_PC("alipay_pc"),
    WXPAU_H5("wxpay_h5"),
    PAYPAL("paypal"),
    EPUSDT("epusdt");

    PaysEnmu(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static PaysEnmu getByValue(String value) {
        for (PaysEnmu paysEnmu : values()) {
            if (paysEnmu.getCode().equals(value)) {
                return paysEnmu;
            }
        }
        return null;
    }

}
