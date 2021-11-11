package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import dtu.group42.server.db.UserTableColumn;
import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.helpers.JsonHelper;
import dtu.group42.server.models.Operation;

@Service("ACService:rbac")
public class RBACService extends AccessControlService {

    public void init() throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType {
        if(super.initialized) return;
        loadPolicy();
    }

    private void loadPolicy()
            throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType {
        var policy = loadPolicyFile();

        var roles = JsonHelper.getStringList(policy, "roles");
        var rolePermissions = policy.getJSONObject("access_control_list");
        validatePolicy(roles, rolePermissions);

        for (var role : roles) {
            populateRoleAccessRights(role.toString(), rolePermissions);
        }
    }

    private void validatePolicy(List<String> roles, JSONObject policies) throws InvalidAccessPolicyException {
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
            roleAccess.add(parseOperationName(specificAccess.toString()));
        }

        _permissions.put(role, roleAccess);
    }

    public boolean verifyAccess(String user, Operation op) throws SQLException{
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
}
