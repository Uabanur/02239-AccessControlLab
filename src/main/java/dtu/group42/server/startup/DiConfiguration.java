package dtu.group42.server.startup;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.services.IAccessControlService;

@Configuration
public class DiConfiguration {
    @Autowired
    ApplicationContext ctx;

    @Bean
    public IAccessControlService ACService(@Value("${policy.type}") String policyType)
            throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType {
        var service = (IAccessControlService) ctx.getBean("ACService:" + policyType);
        service.init();
        return service;
    }
}
