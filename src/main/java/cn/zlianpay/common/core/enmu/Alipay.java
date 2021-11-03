package cn.zlianpay.common.core.enmu;

public enum Alipay {
    MQPAY_ALIPAY("mqpay_alipay"),
    ZLIANPAY_ALIPAY("zlianpay_alipay"),
    YUNGOUOS_ALIPAY("yungouos_alipay"),
    XUNHUPAY_ALIPAY("xunhupay_alipay"),
    JIEPAY_ALIPAY("jiepay_alipay"),
    PAYJS_ALIPAY("payjs_alipay"),
    ALIPAY("alipay"),
    ALIPAY_PC("alipay_pc");

    Alipay(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static boolean getByValue(String value) {
        for (Alipay alipay : values()) {
            if (alipay.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
