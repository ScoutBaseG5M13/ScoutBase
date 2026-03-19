package es.dimecresalessis.scoutbase;

import es.dimecresalessis.scoutbase.infrastructure.security.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ScoutbaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScoutbaseApplication.class, args);
	}
}
