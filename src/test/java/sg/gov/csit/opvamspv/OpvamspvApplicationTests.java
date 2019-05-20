package sg.gov.csit.opvamspv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sg.gov.csit.opvamspv.officer.OfficerController;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpvamspvApplicationTests {

	@Autowired
	private OfficerController officerController;

	@Test
	public void contextLoads() throws Exception{

		assertThat(officerController).isNotNull();


	}

}

