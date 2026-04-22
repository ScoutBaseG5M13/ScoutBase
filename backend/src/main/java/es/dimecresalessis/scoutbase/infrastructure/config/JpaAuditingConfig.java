package es.dimecresalessis.scoutbase.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Infrastructure configuration to enable automated auditing for JPA entities.
 * <p>
 * This class activates Spring Data JPA's auditing capabilities, allowing the
 * system to automatically track metadata such as creation and modification
 * timestamps on entities marked with auditing annotations.
 * </p>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {}