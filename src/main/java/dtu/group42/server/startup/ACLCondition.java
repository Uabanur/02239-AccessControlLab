package dtu.group42.server.startup;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;

@PropertySource("classpath:application.properties")
public class ACLCondition implements Condition{
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var policyType = context.getEnvironment().getProperty("policy.type");
        return "acl".equalsIgnoreCase(policyType);
    }
}
