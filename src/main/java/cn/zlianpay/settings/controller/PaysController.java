package cn.zlianpay.settings.controller;

import cn.zlianpay.common.core.web.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.zlianpay.common.core.web.*;
import cn.zlianpay.common.core.annotation.OperLog;
import cn.zlianpay.settings.entity.Pays;
import cn.zlianpay.settings.service.PaysService;
import cn.zlianpay.settings.vo.PaysVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付配置管理
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
@Controller
@RequestMapping("/settings/pays")
public class PaysController extends BaseController {

    @Autowired
    private PaysService paysService;

    @RequiresPermissions("settings:pays:view")
    @RequestMapping()
    public String view() {
        return "settings/pays.html";
    }

    /**
     * 分页查询支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<PaysVo> page(HttpServletRequest request) {
        PageParam<Pays> pageParam = new PageParam<>(request);
        List<Pays> paysList = paysService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<PaysVo> collect = paysList.stream().map((pays) -> {
            PaysVo paysVo = new PaysVo();
            BeanUtils.copyProperties(pays, paysVo);
            JSONObject configs = JSONObject.parseObject(pays.getConfig());
            if (pays.getDriver().equals("codepay_alipay") || pays.getDriver().equals("codepay_wxpay")) {
                paysVo.setAppid(configs.get("id").toString());
                paysVo.setKey(configs.get("key").toString());
                paysVo.setNotifyUrl(configs.get("notify_url").toString());
            } else if (pays.getDriver().equals("mqpay_alipay") || pays.getDriver().equals("mqpay_wxpay")) {
                paysVo.setKey(configs.get("key").toString());
                paysVo.setCreateUrl(configs.get("create_url").toString());
                paysVo.setNotifyUrl(configs.get("notify_url").toString());
            } else if (pays.getDriver().equals("zlianpay_alipay") || pays.getDriver().equals("zlianpay_wxpay")) {
                paysVo.setAppid(configs.get("pid").toString());
                paysVo.setKey(configs.get("key").toString());
                paysVo.setCreateUrl(configs.get("create_url").toString());
                paysVo.setNotifyUrl(configs.get("notify_url").toString());
            } else if (pays.getDriver().equals("yungouos_wxpay") || pays.getDriver().equals("yungouos_alipay")) {
                paysVo.setAppid(configs.get("mchId").toString());
                paysVo.setKey(configs.get("key").toString());
                paysVo.setNotifyUrl(configs.get("notify_url").toString());
            }
            return paysVo;
        }).collect(Collectors.toList());
        return new PageResult<>(collect, pageParam.getTotal());
    }

    /**
     * 查询全部支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Pays> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(paysService.list(pageParam.getOrderWrapper()));
        //List<Pays> records = paysService.listAll(pageParam.getNoPageParam());  // 使用关联查询
        //return JsonResult.ok().setData(pageParam.sortRecords(records));
    }

    /**
     * 根据id查询支付配置
     */
    @RequiresPermissions("settings:pays:list")
    @OperLog(value = "支付配置管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(paysService.getById(id));
    }

    /**
     * 添加支付配置
     */
    @RequiresPermissions("settings:pays:save")
    @OperLog(value = "支付配置管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Pays pays) {
        if (paysService.save(pays)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改支付配置
     */
    @RequiresPermissions("settings:pays:update")
    @OperLog(value = "支付配置管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(PaysVo paysVo) {

        Map<String,String> map = new HashMap<>();
        if (paysVo.getDriver().equals("codepay_alipay") || paysVo.getDriver().equals("codepay_wxpay") || paysVo.getDriver().equals("codepay_qqpay")) {
            map.put("id", paysVo.getAppid());
            map.put("key", paysVo.getKey());
            map.put("notify_url", paysVo.getNotifyUrl());
        } else if (paysVo.getDriver().equals("mqpay_alipay") || paysVo.getDriver().equals("mqpay_wxpay")) {
            map.put("key", paysVo.getKey());
            map.put("create_url", paysVo.getCreateUrl());
            map.put("notify_url", paysVo.getNotifyUrl());
        } else if (paysVo.getDriver().equals("zlianpay_alipay") || paysVo.getDriver().equals("zlianpay_wxpay")) {
            map.put("pid", paysVo.getAppid());
            map.put("key", paysVo.getKey());
            map.put("create_url", paysVo.getCreateUrl());
            map.put("notify_url", paysVo.getNotifyUrl());
        } else if (paysVo.getDriver().equals("yungouos_wxpay") || paysVo.getDriver().equals("yungouos_alipay")) {
            map.put("mchId", paysVo.getAppid());
            map.put("key", paysVo.getKey());
            map.put("notify_url", paysVo.getNotifyUrl());
        }
        String jsonString = JSON.toJSONString(map);

        Pays pays = new Pays();
        BeanUtils.copyProperties(paysVo,pays);
        pays.setConfig(jsonString);

        if (paysService.updateById(pays)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除支付配置
     */
    @RequiresPermissions("settings:pays:remove")
    @OperLog(value = "支付配置管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (paysService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加支付配置
     */
    @RequiresPermissions("settings:pays:save")
    @OperLog(value = "支付配置管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Pays> list) {
        if (paysService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改支付配置
     */
    @RequiresPermissions("settings:pays:update")
    @OperLog(value = "支付配置管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Pays> batchParam) {
        if (batchParam.update(paysService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除支付配置
     */
    @RequiresPermissions("settings:pays:remove")
    @OperLog(value = "支付配置管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (paysService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改状态", result = true)
    @RequiresPermissions("products:products:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer enabled) {
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Pays pays = new Pays();
        pays.setId(id);
        pays.setEnabled(enabled);
        if (paysService.updateById(pays)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

}
