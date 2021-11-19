package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.util.HashSet;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.helpers.ArrayHelper;
import dtu.group42.server.helpers.JsonHelper;
import dtu.group42.server.models.Operation;

@Service("ACService:acl")
public class ACLService extends AccessControlService{
    @Override
    protected void loadPolicy() throws InvalidAccessPolicyException, FileNotFoundException, InvalidAccessPolicyType {
        var policy = loadPolicyFile();
        var userAccessRights = policy.getJSONObject("access_control_list");
        validatePolicy(userAccessRights);

        for (var user : userAccessRights.keySet()) {
            populateUserAccessRights(user, userAccessRights);
        }
    }

    private void validatePolicy(JSONObject userAccessRights) throws InvalidAccessPolicyException {
        for (String user : userAccessRights.keySet()) {
            var accessRights = JsonHelper.getStringList(userAccessRights, user);
            for (String operation : accessRights) {
                try {
                    Operation.valueOf(operation);
                } catch (RuntimeException e) {
                    throw new InvalidAccessPolicyException("Undefined operation: " + operation);
                }
            }
        }
    }

    private void populateUserAccessRights(String user, JSONObject userAccessRights) {
        var userAccess = new HashSet<Operation>();
        if (!userAccessRights.has(user))
            return;

        var accessRights = JsonHelper.getStringList(userAccessRights, user);
        for (var operation : accessRights) {
            userAccess.add(parseOperationName(operation));
        }

        _permissions.put(user, userAccess);
    }

    public boolean verifyAccess(String user, Operation op) {
        return _permissions.containsKey(user) && _permissions.get(user).contains(op);
    }

    @Override
    public String[] getAccessForUser(String username){
        if(!_permissions.containsKey(username)) return new String[]{};
        return ArrayHelper.map(_permissions.get(username), x -> x.toString()).toArray(String[]::new);
    }
}
