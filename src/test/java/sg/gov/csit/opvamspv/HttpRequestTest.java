package sg.gov.csit.opvamspv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sg.gov.csit.opvamspv.accesscontrollist.Role;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerProfileDto;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sg.gov.csit.opvamspv.seeding.DataSeeder;
import sg.gov.csit.opvamspv.testconfig.JwtToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebMvcTest(OfficerController.class)
//@DirtiesContext
public class HttpRequestTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

//    @Autowired
//    private OfficerRepository officerRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DataSeeder dataSeeder;

    final String baseAuthenUrl = "http://yahoo.com";

    private HttpHeaders headers;

    @Test
    public void init(){

//        dataSeeder.seed(null);

        //code to get cookie
        JwtToken jwttoken = new JwtToken();
        jwttoken.setUserName("Hello");
        jwttoken.setPassword("Password123");

        HttpEntity<JwtToken> request = new HttpEntity<>(jwttoken);
        HttpEntity<String> response = restTemplate.exchange(baseAuthenUrl, HttpMethod.POST, request, String.class);
        this.headers = response.getHeaders();
//        String jwtToken = headers.getFirst(headers.SET_COOKIE);


//        Officer officer = new Officer();
//
//        officer.setPf("11011");
//        officer.setName("Tan Miam Miam");
//        officer.setRole(Role.AO);
//        officer.setAdmin(true);
//        officerRepository.save(officer);
    }


    @Test
    public void officerProfileShouldReturnOfficerDto() throws Exception {

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/Officer/Profile",
                GET,
                new HttpEntity<String>(headers),
                String.class);

        System.out.println("Response: " + response);


        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/Officer/Profile",
//                String.class)).contains("11011");


    }


//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OfficerRepository officerRepository;

//    @Test
//    public void officerProfileShouldReturnOfficeDto() throws Exception {
//
//        when(officerRepository.findById("")).thenReturn(java.util.Optional.of(new Officer("11011", "Joshua", Role.Admin)));
//
//        this.mockMvc.perform(get("/api/v1/Officer/Profile")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("11011")));
//    }

}
