#!groovy

import jenkins.model.Jenkins
import hudson.security.PermissionGroup
import hudson.security.Permission
import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy
import com.michelin.cio.hudson.plugins.rolestrategy.Role
import com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType
import org.jenkinsci.plugins.rolestrategy.permissions.PermissionHelper

Jenkins jenkins = Jenkins.getInstance()
def rbas = new RoleBasedAuthorizationStrategy()

/* create admin role */
Set<Permission> permissions = new HashSet<>();
def groups = new ArrayList<>(PermissionGroup.getAll());
groups.remove(PermissionGroup.get(Permission.class));
Role adminRole = new Role("admin",permissions)

/* assign admin role to admin user */
globalRoleMap = rbas.getRoleMaps()[RoleType.Global]
globalRoleMap.addRole(adminRole)
globalRoleMap.assignRole(adminRole, 'okurylo')

jenkins.setAuthorizationStrategy(rbas)

// power user
def folderRoleAdmin = "poweruser"
def folderPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read"
]
Set<Permission> folderPermissionSet = new HashSet<Permission>();
folderPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    folderPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}
Role poweruserRole = new Role(folderRoleAdmin, folderPermissionSet);
rbas.addRole(RoleType.Global, poweruserRole);
rbas.assignRole(RoleType.Global, poweruserRole)


jenkins.save()