package server;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

public class AccesControlManager {
    private static Connection _connection;
    private static Map<String, Set<Operation>> _permissions;

    public static void init() throws SQLException, FileNotFoundException, InvalidAccessPolicyException {
        connect();
        loadAccessPolicies();
    }

    private static void loadAccessPolicies() throws FileNotFoundException, InvalidAccessPolicyException {
        var jsonObj = JsonHelper.load("access_policy.json");
        var roles = JsonHelper.getStringList(jsonObj, "roles");
        var policies = jsonObj.getJSONObject("access_control_list");
        validatePolicy(roles, policies);

        _permissions = new HashMap<String, Set<Operation>>();
        for (var role : roles) {
            populateRoleAccessRights(role.toString(), policies);
        }
    }

    private static void validatePolicy(List<String> roles, JSONObject policies) throws InvalidAccessPolicyException {
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

    private static void populateRoleAccessRights(String role, JSONObject policies) {
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

    private static Operation ParseOperationName(String operationName) {
        return Operation.valueOf(operationName);
    }

    private static void connect() throws SQLException {
        _connection = AuthDatabase.getConnection();
    }

    public static boolean verifyAccessForUser(String username, Operation op) throws SQLException {
        if (_connection == null)
            connect();

        // get user from db
        String query = "select roles from users where username = ?";
        PreparedStatement statement = _connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
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
