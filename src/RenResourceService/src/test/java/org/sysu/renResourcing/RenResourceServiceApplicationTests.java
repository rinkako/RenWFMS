package org.sysu.renResourcing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.sysu.renResourcing.statistics.LogMiner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenResourceServiceApplicationTests {

	@Test
	public void TestDurationsRetrieve() {
        List<Long> ret = LogMiner.GetTaskExecutionDurationsForParticipant(
                "TSK_4e04df49-b08c-4d75-8ede-c5c4dcf79232",
                "Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2", "");
	}

}
