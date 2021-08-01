package cn.zlianpay.common.core.pays.yunfupay;

import cn.hutool.crypto.SecureUtil;
import java.util.Map;

public class SignUtil {

    /**
     * 生成密钥
     * @param params
     * @param privateKey
     * @return
     */
    public static String createSign(Map<String, Object> params, String privateKey) {
        // 定义 sb 为了获取 MD5 加密前的字符串
        StringBuilder sb = new StringBuilder();
        // 将HashMap 进行 键 的 Ascll 从小到大排序 并 将每个 hashmap元素 以 & 拼接起来
        params.entrySet().stream().sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey())).forEach(a ->{
            sb.append(a).append("&");});
        // 去除 最后一位的 &
        sb.deleteCharAt(sb.length()-1);
        // 拼接上密钥
        sb.append(privateKey);
        // 调用 Hutool 的 加密工具 进行 MD5 加密
        String s = SecureUtil.md5(sb.toString());
        return s;
    }

}
