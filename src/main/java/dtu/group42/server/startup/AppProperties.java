package dtu.group42.server.startup;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:application.properties")
public class AppProperties {
}
