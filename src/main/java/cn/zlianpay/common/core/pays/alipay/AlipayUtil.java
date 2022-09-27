package cn.zlianpay.common.core.pays.alipay;

import cn.zlianpay.settings.entity.Pays;
import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * author：panyoujie
 * date：2022-0902
 */
public class AlipayUtil {

    private static Logger logger = LoggerFactory.getLogger(AlipayUtil.class);

    /**
     * 支付宝当面付
     * @param subject 自定义内容
     * @param orderNo 订单号
     * @param totalAmount 订单金额
     * @return
     */
    public static String getFaceToFace(Pays pays, String subject, String orderNo, String totalAmount) {
        // 获取支付配置信息
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String app_id = mapTypes.get("app_id").toString();
        String private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String notify_url = mapTypes.get("notify_url").toString() + "/alipay/notify";   // 异步通知地址

        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(app_id, private_key, alipay_public_key, notify_url));
        try {
            // 2. 发起API调用（使用面对面支付中的预下单）
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate(subject, orderNo, totalAmount);
            // 3. 处理响应或异常
            if ("10000".equals(response.code)) {
                logger.info("调用成功：{}", response.qrCode);
                return response.qrCode; //返回二维码
            } else {
                logger.error("调用失败，原因：{},{}", response.msg, response.subMsg);
            }
        } catch (Exception e) {
            logger.error("调用遭遇异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 支付宝电脑网站支付
     * @param subject 自定义内容
     * @param orderNo 订单号
     * @param totalAmount 订单金额
     * @return
     */
    public static String getPcPage(Pays pays, String subject, String orderNo, String totalAmount) {
        // 获取支付配置信息
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String app_id = mapTypes.get("app_id").toString();
        String private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String url = mapTypes.get("notify_url").toString();

        /**
         * appid 2021002109658979
         * private_key: MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCB59e1QqLhCDJWHV9J/Uy8DCJWSazRvsC6IXUOHy8GneXhLrMuynGFEnFcMRiYvX438ww611YJrDNN3W8S30+GgfzKpoGhY7DQB4v2lhAshH3r1KyVlZQzJmN+eyX2j58Fj0mW8c2ApQPXtRqemysiU0w4ybYn9pX+r4W9duRFVFyzChW9J02GjLqtp2pCtGTWajbZQ09XW7M0XHQuQ/Ptv8q4ddQPQfrGEgi0Cm5q1iLmw2dE2CsfAN/AX1upEakgx9K6wHUCsqaZZzYpdL2j9h9EljvLl/Vdnf3znlKCvE7DgmCvorLZgUximrbNkwWEzluEMux4Hx7aqH7xP0DDAgMBAAECggEAGf6EgXbWFOWI/QZfnPScuDxNWqrfdBERi61KOQswY8iQyWCI5mIIGTK5kFMasuWoLhXqapPQWZsloP0gTsx0o1u7c+mhouPJ67a24R7iGaZhdCFYb45A24NhAVwvGolersQfW58DGizq+ez5aKdwGCxG4k4qrxB+rzn8Lw9tWhngViQm4i0UEwQvgEwjeJ3zZZeAAtjnNeLAe6d1AP4bKh6Po5HrQYd+5gpEV5J9uBB3Ouem+NZwYXSeTfGyRdYCHHWRRWsNRGRvG3VjVhKQYgueYTj6vIoTpp8nEeW4ZwC4GybSfJsBc/cdzfkrDDJvuv3Iu1PUkc2OB/ZZIrltQQKBgQDC3hHJ7gQ60TimLuEpwDOE/RuDK5totgO10Km1DL3Sg5So0dlyoLXWqpYxVt/QRHPlO6wpFCemCLGymN4fmghNaZYhfgPZ78R+Dnt6BdX/70ibFw9EbT4dv9lOlx6Fm1eKltuH8reP/4xZ4+vSFfcX0hSQ4hQ60w1m+SPDzoXekwKBgQCqqKW9pmCtRlOF+0mEO7sqCb3I6RlvQ0CspGR0PTjm4tB7JhoomNrDhqHIafTA2P+YTDNvD7/W9fZ6InfmNsas1hUyXqh8UctNrIbLkkSLaE+mhMKVp7wHn+ZibXeYfr0O519oCL+aVzt2R7tT9gI4eafMaFOPI3xOjevv8FlDEQKBgFuQEPe6+Q3SBTZAzNfbUdnpPCY0NT/MVo31z+gcZo0l+pIgmHX+AbMuUIg6c8vefj51/X0H1WF0sFpk7eoXbFNDi6GNHq4mtUMt9uLtHxK1En6dJTVXx1ofLTJ6W4MX7NhX7itA5vOodgA+0i3S2Fm/Ap7ZO/wv8xoxcpas0cY1AoGANHssjQfvTxoDcX53ezzLMABS5sgYVEkASJc9dSmmqAo0RsrO3JE1JU/vzKwY4n1ZDMLSRmM3gameJK0Y5ikOK/SiDB1j/udAeCCIS24tuhlX9UN6e39bqZSYysgTMUg4Z67rfhjCo3oUBSnGB+ntgOSYn5mtqKUnAxlN5zmomaECgYBw3Jm0zty3pi9gb8sGeKV1/Gu8ig5L/dX6lCqjogA4U80p916k7IWG5igwbvWjasaD62BOIYJaWhqSODqEfrnLnk5pdrZNPwjAYAZTkhvkjqK33NW7Ow6IieIVryMlN4HUHwz/S9GgzQ6QiJRMM+wp/Z9lbfd74O/aq2h8cWIGoA==
         * alipay_public_key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnGiVlrrXv+asJViQHMxCFgYSWE/mGo/u+gEeY98eYM3IecYvq2ATB76OeFPtGeaea7yHTnUcVlc0rzOEwYsub4PpyPJP8W2WEeaxayXpX2jLqQf2EqNxBpRcrRnGSZP37K85ub8xKuBAnMtIHD76U3xkSHhZZqnJdlqe9PFwWW4mYxaX0OMytsTpQqCZbJMmNT2H/rQTO0vf4vrkcU79xjx7QLhylOBMnkbi9mGo06ZPom+wb9bRrA7qP0HJojRDbGPfue0jq6qljTL6ymG3LnYOBo4B1ecraOV2/c9qmlNteoAqjgPwFGQDTUj2Ujvmf3VToV67cFjWfLt/G7ogpwIDAQAB
         * 通知地址: http://iapp.natapp1.cc
         */
        String notify_url = url + "/alipay/notify";
        String return_url = url + "/alipay/return_url";

        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(app_id, private_key, alipay_public_key, notify_url));
        try {
            // 2. 发起API调用（使用面对面支付中的预下单）
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(subject, orderNo, totalAmount, return_url);
            // 3. 处理响应或异常
            return response.getBody();
        } catch (Exception e) {
            logger.error("调用遭遇异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Config getOptions(String appId, String privateKey, String publicKey, String notifyUrl) {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";

        // 请更换为您的AppId
        config.appId = appId;
        // 请更换为您的PKCS8格式的应用私钥
        config.merchantPrivateKey = privateKey;
        config.alipayPublicKey = publicKey;
        // 如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.notifyUrl = notifyUrl; //这里是支付宝接口回调地址

        return config;
    }

}
