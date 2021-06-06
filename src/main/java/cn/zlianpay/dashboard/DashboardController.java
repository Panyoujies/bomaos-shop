package cn.zlianpay.dashboard;

import cn.zlianpay.common.core.web.BaseController;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.zlianpay.common.core.utils.DateUtil;
import cn.zlianpay.common.core.utils.RmbUtil;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @RequiresPermissions("dashboard:user:view")
    @RequestMapping("/workplace")
    public String view(Model model) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 获取今天的开始时间到结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayStartTime = formatter.format(DateUtil.getDayBegin());
        String todayEndTime = formatter.format(DateUtil.getDayEnd());
        Map<String, Object> toDayTotal = getTimeDayList(todayStartTime, todayEndTime, ordersService);

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String DayStartTime = formatter.format(DateUtil.getStartDayTime(-+i));
            String DayEndTime = formatter.format(DateUtil.getEndDayTime(-+i));
            Map<String, Object> DayTotal = getTimeDayList(DayStartTime, DayEndTime, ordersService);
            String day = simpleDateFormat.format(DateUtil.getStartDayTime(-+i));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("totalSumlong", DayTotal);
            map1.put("day", day);
            list.add(map1);
        }

        // 昨天的开始时间和结束时间
        String yesterDayStartTime = formatter.format(DateUtil.getStartDayTime(-1));
        String yesterDayEndTime = formatter.format(DateUtil.getEndDayTime(-1));
        Map<String, Object> yesterDayTotal = getTimeDayList(yesterDayStartTime, yesterDayEndTime, ordersService);

        // 本周的开始时间和本周的结束时间
        String BeginDayStartTime = formatter.format(DateUtil.getBeginDayOfWeek());
        String EndDayEndTime = formatter.format(DateUtil.getEndDayOfWeek());
        Map<String, Object> OfWeekDayTotal = getTimeDayList(BeginDayStartTime, EndDayEndTime, ordersService);

        // 这七天的数据
        List<Object> data = new ArrayList<>();
        // 近期七天的交易金额统计
        List<Object> day = new ArrayList<>();
        for (Object o : list) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object entryValue = entry.getValue();
                if (key.equals("totalSumlong")) {
                    Map<Object, Object> value = (Map<Object, Object>) entryValue;
                    data.add(value.get("totalSumlong"));
                } else if (key.equals("day")) {
                    day.add(entry.getValue());
                }
            }
        }

        getPayTypeCount(null,null);


        List<Map> countlist = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String DayStartTime = formatter.format(DateUtil.getStartDayTime(-+i));
            String DayEndTime = formatter.format(DateUtil.getEndDayTime(-+i));
            Map payTypeCount = getPayTypeCount(DayStartTime, DayEndTime);
            countlist.add(payTypeCount);
        }

        List<Integer> alipayCount = new ArrayList<>();

        for (Map map : countlist) {
            alipayCount.add((Integer) map.get("alipayCount"));
        }

        List<Integer> wxCount = new ArrayList<>();
        for (Map map : countlist) {
            wxCount.add((Integer) map.get("wxCount"));
        }

        model.addAttribute("alipayCount", JSON.toJSONString(alipayCount)); // 显示哪天 截止七天前
        model.addAttribute("wxCount", JSON.toJSONString(wxCount)); // 显示这七天的数据

        model.addAttribute("day", JSON.toJSONString(day)); // 显示哪天 截止七天前
        model.addAttribute("data", JSON.toJSONString(data)); // 显示这七天的数据

        Map<String, Object> map = new HashMap<>();
        map.put("toDayTotal", toDayTotal.get("totalSumlong")); // 今天的订单金额
        map.put("yesterDayTotal", yesterDayTotal.get("totalSumlong")); // 昨天的订单金额
        map.put("OfWeekDayTotal", OfWeekDayTotal.get("totalSumlong")); // 本周的订单成交金额
        map.put("longTotal", toDayTotal.get("longTotal")); // 本周的订单成交金额

        model.addAttribute("orderTimeTotal", map);

        return "dashboard/workplace.html";
    }

    public Map<String, Object> getTimeDayList(String StartTime, String EndTime, OrdersService service) {

        QueryWrapper queryWrapper = getQueryWrapper(StartTime,EndTime);

        //查询当天所有支付记录
        List<Orders> ordersList = service.list(queryWrapper);

        // 今天的收款金额
        Map map = RmbUtil.getRmbCount(ordersList);

        return map;
    }

    private QueryWrapper getQueryWrapper(String StartTime, String EndTime) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);

        if (StartTime != null || EndTime != null) {
            //查询条件为时间范围
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + StartTime + "')");
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) < UNIX_TIMESTAMP('" + EndTime + "')");
        }

        return queryWrapper;
    }

    public Map getPayTypeCount(String StartTime, String EndTime) {

        QueryWrapper queryWrapper = getQueryWrapper(StartTime,EndTime);

        // 查询支付宝
        List<Orders> list = ordersService.list(queryWrapper); // 已付款

        Integer alipayCount = 0;
        Integer wxCount = 0;
        for (Orders orders : list) {
            String payType = orders.getPayType(); // 支付类型
            if (payType.equals("mqpay_alipay") || payType.equals("alipay") || payType.equals("yungouos_alipay") || payType.equals("codepay_alipay") || payType.equals("zlianpay_alipay")) {
                alipayCount++;
            } else if (payType.equals("codepay_wxpay") || payType.equals("yungouos_wxpay") || payType.equals("mqpay_wxpay") || payType.equals("zlianpay_wxpay")) {
                wxCount++;
            }
        }

        Map<String,Integer> map = new HashMap<>();
        map.put("alipayCount",alipayCount);
        map.put("wxCount",wxCount);

        return map;
    }

}
