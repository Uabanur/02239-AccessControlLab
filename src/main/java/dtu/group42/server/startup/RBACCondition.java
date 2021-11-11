package dtu.group42.server.startup;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RBACCondition implements Condition{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var policyType = context.getEnvironment().getProperty("policy.type");
        return "rbac".equalsIgnoreCase(policyType);
    }
}
