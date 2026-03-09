package es.dimecresalessis.scoutbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScoutbaseApplication {

	private static final Logger logger = LoggerFactory.getLogger(ScoutbaseApplication.class);

	public static void main(String[] args) {
		logger.info("--- APPLICATION STARTING LOGGER OUCH! 14:47h---");
		SpringApplication.run(ScoutbaseApplication.class, args);
	}
}
