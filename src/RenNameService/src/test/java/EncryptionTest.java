/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.sysu.renNameService.RenNameServiceApplication;
import org.sysu.renCommon.utility.EncryptUtil;

/**
 * Author: Rinkako
 * Date  : 2018/1/29
 * Usage :
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RenNameServiceApplication.class)
@WebAppConfiguration
public class EncryptionTest {
    @Test
    public void TestEncryption() {
        String encrypted = EncryptUtil.EncryptSHA256("123456");
        System.out.println(encrypted);
    }
}
