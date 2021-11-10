package cn.zlianpay.carmi.service.impl;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.mapper.CardsMapper;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.utils.ExcelPOJO;
import cn.zlianpay.carmi.utils.ExcelWrite;
import cn.zlianpay.carmi.vo.CardsDts;
import cn.zlianpay.common.core.Constants;
import cn.zlianpay.common.core.utils.DateUtil;
import cn.zlianpay.common.core.web.JsonResult;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.mapper.ProductsMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 卡密服务实现类
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
@Service
@Transactional
public class CardsServiceImpl extends ServiceImpl<CardsMapper, Cards> implements CardsService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public PageResult<Cards> listPage(PageParam<Cards> page) {
        List<Cards> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Cards> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public JsonResult addCards(CardsDts cardsDts) {
        if (cardsDts.getSellType() == 0) { // 单卡销售
            String[] cardsInfo = cardsDts.getCardInfo().split("\\n");

            List<String> newlist = new ArrayList();
            for (int i = 0; i < cardsInfo.length; ++i) {
                if (cardsDts.getRepeat() == 0) {
                    newlist.add(cardsInfo[i]);
                } else if (cardsDts.getRepeat() == 1 && !newlist.contains(cardsInfo[i])) {
                    newlist.add(cardsInfo[i]);
                }
            }

            List<Cards> cardsArrayList = new ArrayList<>();
            for (String cardInfo : newlist) {
                Cards cards = new Cards();
                cards.setProductId(cardsDts.getProductId());
                cards.setCardInfo(cardInfo);
                cards.setStatus(0); // 设置未出售
                cards.setSellType(0);
                cards.setNumber(1);
                cards.setSellNumber(0);
                cards.setCreatedAt(new Date());
                cards.setUpdatedAt(new Date());
                cardsArrayList.add(cards);
            }

            boolean batch = this.saveBatch(cardsArrayList);
            if (batch) {
                return JsonResult.ok("批量添加卡密成功！");
            }
            return JsonResult.error("添加卡密失败");
        } else { // 重复销售

            Cards cards1 = this.getOne(new QueryWrapper<Cards>().eq("product_id", cardsDts.getProductId()).eq("status", 0));
            if (!ObjectUtils.isEmpty(cards1)) {
                return JsonResult.error("当前商品为重复销售类型、已存在一个重复销售的卡密、请勿重复添加，如需修改当前卡密数量请前往卡密管理进行操作。");
            }

            Cards cards = new Cards();
            cards.setProductId(cardsDts.getProductId());
            cards.setCardInfo(cardsDts.getCardInfo());
            cards.setStatus(0); // 设置未出售
            cards.setSellType(1);
            cards.setNumber(cardsDts.getSellNumber());
            cards.setSellNumber(0);
            cards.setCreatedAt(new Date());
            cards.setUpdatedAt(new Date());
            boolean save = this.save(cards);
            if (save) {
                return JsonResult.ok("重复销售卡密添加成功！");
            }
            return JsonResult.error("重复销售的卡密添加失败。");
        }
    }

    @Override
    public List<Cards> getCard(Integer status, Integer productId, Integer number) {
        return baseMapper.getCard(status, productId, number);
    }

    @Override
    public String export(Integer productId, Integer status) {

        /**
         * 查询需要导出的卡密列表
         */
        List<Cards> cardsList = baseMapper.selectList(new QueryWrapper<Cards>().eq("product_id", productId).eq("status", status));

        List<ExcelPOJO> list = new ArrayList<>();
        for (Cards cards : cardsList) {
            Products products = productsMapper.selectById(cards.getProductId());

            ExcelPOJO excelPOJO = new ExcelPOJO();
            excelPOJO.setName(products.getName()); // 商品名称
            excelPOJO.setCode(cards.getCardInfo()); // 卡密信息
            excelPOJO.setTime(DateUtil.getDate());

            list.add(excelPOJO);
        }

        String[] arr = {"商品名称", "卡密信息", "导出时间"};
        String toExcelByPOJO = ExcelWrite.writeToExcelByPOJO(Constants.UPLOAD_DIR + "file/excel/", arr, list);

        return toExcelByPOJO;
    }

}
