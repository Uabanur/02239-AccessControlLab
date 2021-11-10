package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtu.group42.server.db.IDatabase;
import dtu.group42.server.db.UserTableColumn;
import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.helpers.JsonHelper;
import dtu.group42.server.models.Operation;

@Service
public class AccessControlService implements IAccessControlService {
    @Autowired
    IDatabase db;

    private Map<String, Set<Operation>> _permissions;
    private boolean initialized = false;
    private String policyType = "rbac";

    @PostConstruct
    public void init()
            throws SQLException, FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType {
        if (initialized)
            return;
        db.init();
        loadAccessPolicies();
        initialized = true;
    }

    private void loadAccessPolicies()
            throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType {
        var policyFileName = "access_policy_" + policyType + ".json";
        var jsonObj = JsonHelper.load(policyFileName);
        var loadedType = jsonObj.getString("type");
        if (!policyType.equals(loadedType))
            throw new InvalidAccessPolicyType(String.format("Loaded policy type: %s does not match expected policy type: %s", loadedType, policyType));

        switch (policyType.toLowerCase()) {
        case "acl":
            loadACL(jsonObj);
            return;
        case "rbac":
            loadRBAC(jsonObj);
            return;
        default:
            throw new InvalidAccessPolicyType("Unknown policy type: " + policyType);
        }
    }

    /// ACL Management
    private void loadACL(JSONObject jsonObj) throws InvalidAccessPolicyException {
        var userAccessRights = jsonObj.getJSONObject("access_control_list");
        validateACLPolicy(userAccessRights);

        _permissions = new HashMap<String, Set<Operation>>();
        for (var user : userAccessRights.keySet()) {
            populateUserAccessRights(user, userAccessRights);
        }
    }

    private void validateACLPolicy(JSONObject userAccessRights) throws InvalidAccessPolicyException {
        for (String user : userAccessRights.keySet()) {
            var accessRights = JsonHelper.getStringList(userAccessRights.getJSONObject(user), "specific");
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

        var accessRights = JsonHelper.getStringList(userAccessRights.getJSONObject(user), "specific");
        for (var operation : accessRights) {
            userAccess.add(ParseOperationName(operation));
        }

        _permissions.put(user, userAccess);
    }

    private boolean checkAclAccess(String user, Operation op) throws InvalidAccessPolicyType{
        if(policyType != "acl") throw new InvalidAccessPolicyType("Trying to use acl rights when loaded policy is not acl");
        return _permissions.containsKey(user) && _permissions.get(user).contains(op);
    }


    /// RBAC Management
    private void loadRBAC(JSONObject jsonObj) throws InvalidAccessPolicyException {
        var roles = JsonHelper.getStringList(jsonObj, "roles");
        var policies = jsonObj.getJSONObject("access_control_list");
        validateRBACPolicy(roles, policies);

        _permissions = new HashMap<String, Set<Operation>>();
        for (var role : roles) {
            populateRoleAccessRights(role.toString(), policies);
        }
    }

    private void validateRBACPolicy(List<String> roles, JSONObject policies) throws InvalidAccessPolicyException {
        for (String roleDefinition : policies.keySet()) {
            if (!roles.contains(roleDefinition))
                throw new InvalidAccessPolicyException("Undefined role in policies: '" + roleDefinition + "'.");

            var rolePolicy = policies.getJSONObject(roleDefinition);
            for (String inheritedRole : JsonHelper.getStringList(rolePolicy, "inherits")) {
                if (!roles.contains(inheritedRole))
                    throw new InvalidAccessPolicyException("Undefined role in inheritance: '" + inheritedRole
                            + "'. Super role: '" + roleDefinition + "'.");
            }
            var accessRights = JsonHelper.getStringList(rolePolicy, "specific");
            for (String operation : accessRights) {
                try {
                    Operation.valueOf(operation);
                } catch (RuntimeException e) {
                    throw new InvalidAccessPolicyException("Undefined operation: " + operation);
                }
            }
        }
    }

    private void populateRoleAccessRights(String role, JSONObject policies) {
        var roleAccess = new HashSet<Operation>();
        if (!policies.has(role))
            return;

        var rolePolicy = policies.getJSONObject(role);

        for (var inheritedAccess : rolePolicy.getJSONArray("inherits")) {
            String inheritedRoleName = inheritedAccess.toString();
            if (_permissions.get(inheritedRoleName) == null) {
                populateRoleAccessRights(inheritedRoleName, policies);
            }

            var inheritedRoles = _permissions.get(inheritedRoleName);
            if (inheritedRoles != null) {
                roleAccess.addAll(inheritedRoles);
            }
        }

        for (var specificAccess : rolePolicy.getJSONArray("specific")) {
            roleAccess.add(ParseOperationName(specificAccess.toString()));
        }

        _permissions.put(role, roleAccess);
    }



    private boolean checkRbacAccess(String user, Operation op) throws SQLException{
        // get user from db
        var result = db.query("users")
            .select(UserTableColumn.roles)
            .whereEquals(UserTableColumn.username, user)
            .execute();

        if (!result.next())
            return false;

        String[] roles = result.getString("roles").split(",");
        for (String role : roles) {
            if (!_permissions.containsKey(role) || !_permissions.get(role).contains(op))
                continue;
            return true;
        }

        return false;
    }

    /// General
    private static Operation ParseOperationName(String operationName) {
        return Operation.valueOf(operationName);
    }

    public boolean verifyAccessForUser(String username, Operation op) 
        throws SQLException, InvalidAccessPolicyType {

        switch(policyType){
            case "acl": return checkAclAccess(username, op);
            case "rbac": return checkRbacAccess(username, op);
            default: throw new InvalidAccessPolicyType("Unknown policy type: " + policyType);
        }
    }
}
