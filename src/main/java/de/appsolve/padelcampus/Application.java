package de.appsolve.padelcampus;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackageClasses = {Application.class}, exclude = SecurityAutoConfiguration.class)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .sources(Application.class)
                .run(args);
    }

    /*
    required for packaging war. war packaging is required for jsp to work
    see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}