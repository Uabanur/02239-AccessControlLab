package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import dtu.group42.server.db.IDatabase;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.helpers.JsonHelper;
import dtu.group42.server.models.Operation;

public abstract class AccessControlService implements IAccessControlService {
    protected @Autowired Environment env;
    protected @Autowired IDatabase db;
    protected Map<String, Set<Operation>> _permissions = new HashMap<String, Set<Operation>>();
    protected boolean initialized = false;

    protected JSONObject loadPolicyFile() throws FileNotFoundException, InvalidAccessPolicyType{
        var policyFileName = env.getProperty("policy.filename");
        var policyType = env.getProperty("policy.type");
        var policyContent = JsonHelper.load(policyFileName);
        var loadedType = policyContent.getString("type");
        if (!policyType.equals(loadedType))
            throw new InvalidAccessPolicyType(String.format("Loaded policy type: %s does not match expected policy type: %s", loadedType, policyType));

        return policyContent;
    }

    protected Operation parseOperationName(String operationName) {
        return Operation.valueOf(operationName);
    }
}
