
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.sysu.dataStoreService.configService.ZKConfigService;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataStoreTests.class)
public class DataStoreTests {

	@Test
	public void TestZKConfig() throws Exception {
		ZKConfigService config = ZKConfigService.GetInstance();
        List<String> lst = config.GetChildren("RenResourceService");
        Map<String, String> mmp = config.RetrieveAll("RenResourceService");
        String rp = config.Retrieve("RenResourceService", "Port");
        Assert.assertEquals(rp, "10233");
        config.Add("RenResourceService", "Port", "666");
        rp = config.Retrieve("RenResourceService", "Port");
        Assert.assertEquals(rp, "666");
        config.Delete("RenResourceService", "Port");
        rp = config.Retrieve("RenResourceService", "Port");
        Assert.assertEquals(rp, "");
        config.Add("RenResourceService", "Port", "10233");
        rp = config.Retrieve("RenResourceService", "Port");
        Assert.assertEquals(rp, "10233");
	}

}
