package dtu.group42.server.startup;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:app.config")
public class AppConfig {
    // used to load property source by annotation
}
