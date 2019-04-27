package sg.gov.csit.opvamspv;

import org.hibernate.mapping.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sg.gov.csit.opvamspv.officer.Role;

import java.util.List;
import java.util.Set;

@EntityScan(basePackageClasses = { OpvamspvApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing
public class OpvamspvApplication {

    private static final Logger log = LoggerFactory.getLogger(OpvamspvApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OpvamspvApplication.class, args);
    }

    @Bean
    public CommandLineRunner debugModeOnly(OfficerRepository officerRepo) {
        return (args) -> {

                officerRepo.save(new Officer("yosemite//11428", "Joshua Zhang", Role.Admin));

                log.info("Hello world!");

            Set<Role> officerRoles = officerRepo.findByIsAdminUniqueRole(true);

            log.info("The result return: " + officerRoles.size());

            officerRoles.forEach(officer-> {
                log.info("Found officer with Role: " + officer.value() );
            });
        };
    }

}
