package cn.zlianpay.carmi.controller;

import cn.zlianpay.common.core.web.*;
import cn.zlianpay.common.core.web.*;
import cn.zlianpay.common.core.annotation.OperLog;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.OrderCardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单关联卡密表管理
 * Created by Panyoujie on 2021-03-29 22:27:41
 */
@Controller
@RequestMapping("/carmi/orderCard")
public class OrderCardController extends BaseController {
    @Autowired
    private OrderCardService orderCardService;

    @RequiresPermissions("carmi:orderCard:view")
    @RequestMapping()
    public String view() {
        return "carmi/orderCard.html";
    }

    /**
     * 分页查询订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:list")
    @OperLog(value = "订单关联卡密表管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<OrderCard> page(HttpServletRequest request) {
        PageParam<OrderCard> pageParam = new PageParam<>(request);
        return new PageResult<>(orderCardService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
        //return orderCardService.listPage(pageParam);  // 使用关联查询
    }

    /**
     * 查询全部订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:list")
    @OperLog(value = "订单关联卡密表管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<OrderCard> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(orderCardService.list(pageParam.getOrderWrapper()));
        //List<OrderCard> records = orderCardService.listAll(pageParam.getNoPageParam());  // 使用关联查询
        //return JsonResult.ok().setData(pageParam.sortRecords(records));
    }

    /**
     * 根据id查询订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:list")
    @OperLog(value = "订单关联卡密表管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(orderCardService.getById(id));
		// 使用关联查询
        //PageParam<OrderCard> pageParam = new PageParam<>();
		//pageParam.put("id", id);
        //List<OrderCard> records = orderCardService.listAll(pageParam.getNoPageParam());
        //return JsonResult.ok().setData(pageParam.getOne(records));
    }

    /**
     * 添加订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:save")
    @OperLog(value = "订单关联卡密表管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(OrderCard orderCard) {
        if (orderCardService.save(orderCard)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:update")
    @OperLog(value = "订单关联卡密表管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(OrderCard orderCard) {
        if (orderCardService.updateById(orderCard)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:remove")
    @OperLog(value = "订单关联卡密表管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (orderCardService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:save")
    @OperLog(value = "订单关联卡密表管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<OrderCard> list) {
        if (orderCardService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:update")
    @OperLog(value = "订单关联卡密表管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<OrderCard> batchParam) {
        if (batchParam.update(orderCardService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除订单关联卡密表
     */
    @RequiresPermissions("carmi:orderCard:remove")
    @OperLog(value = "订单关联卡密表管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (orderCardService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
