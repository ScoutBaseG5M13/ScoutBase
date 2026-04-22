package es.dimecresalessis.scoutbase;

import es.dimecresalessis.scoutbase.infrastructure.security.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main entry point for the Scoutbase application.
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ScoutbaseApplication {

	/**
	 * The main method that launches the application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScoutbaseApplication.class, args);
	}
}
