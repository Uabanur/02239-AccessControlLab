package dtu.group42.server.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import dtu.group42.server.db.IDatabase;
import dtu.group42.server.db.UserTableColumn;
import dtu.group42.server.exceptions.InvalidAccessPolicyException;
import dtu.group42.server.exceptions.InvalidAccessPolicyType;
import dtu.group42.server.helpers.JsonHelper;
import dtu.group42.server.models.Operation;

public abstract class AccessControlService implements IAccessControlService {
    protected @Autowired Environment env;
    protected @Autowired IDatabase db;
    protected Map<String, Set<Operation>> _permissions = new HashMap<String, Set<Operation>>();
    protected boolean initialized = false;

    public void init() throws FileNotFoundException, InvalidAccessPolicyException, InvalidAccessPolicyType, SQLException {
        if(initialized) return;
        loadPolicy();
        printAccessData();
        initialized = true;
    }

    protected abstract void loadPolicy() throws InvalidAccessPolicyException, FileNotFoundException, InvalidAccessPolicyType;

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

    private void printAccessData() throws SQLException{
        var users = db.query("users")
        .select(UserTableColumn.username)
        .execute();

        System.out.println("Loaded access data:");
        while(users.next()){
            var user = users.getString(UserTableColumn.username.toString());
            System.out.println(String.format("User '%s' has access: %s", user, String.join(", ", getAccessForUser(user))) );
        }
    }

    protected abstract String[] getAccessForUser(String username) throws SQLException;
}
