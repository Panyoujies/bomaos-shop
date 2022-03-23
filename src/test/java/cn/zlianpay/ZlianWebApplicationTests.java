package cn.zlianpay;

import cn.zlianpay.reception.util.SynchronizedByKey;
import cn.zlianpay.reception.util.SynchronizedByKeyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZlianWebApplicationTests {

    @Autowired
    private SynchronizedByKeyService synchronizedByKey;

    @Test
    public void contextLoads() {
        synchronizedByKey.exec("1", () -> {
            System.out.println(11111);
        });
    }

}
