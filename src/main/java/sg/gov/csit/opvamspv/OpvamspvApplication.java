package sg.gov.csit.opvamspv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EntityScan(basePackageClasses = {OpvamspvApplication.class, Jsr310JpaConverters.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class OpvamspvApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpvamspvApplication.class, args);
    }

}
