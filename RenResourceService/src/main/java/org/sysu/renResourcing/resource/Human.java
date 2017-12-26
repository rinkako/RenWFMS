package org.sysu.renResourcing.resource;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2017/12/15
 * Usage : Human Resource Data Package
 */
public class Human extends RResource {
    /**
     * Construct a new Human Entity.
     */
    public Human() {
        this.Role = new ArrayList<>();
    }

    /**
     * Add a role to this human.
     * @param roleId role id
     * @return did operation success
     */
    public boolean AddRole(String roleId) {
        if (this.Role.contains(roleId)) {
            return false;
        }
        this.Role.add(roleId);
        return true;
    }

    /**
     * Remove a role from this human.
     * @param roleId role id
     * @return did operation success
     */
    public boolean RemoveRole(String roleId) {
        if (this.Role.contains(roleId)) {
            this.Role.remove(roleId);
            return true;
        }
        return false;
    }

    /**
     * human person id, unique.
     */
    public String PersonID = "";

    /**
     * human first name, not unique.
     */
    public String FirstName = "";

    /**
     * human last name, not unique.
     */
    public String LastName = "";

    /**
     * A list of role which this human has.
     */
    protected ArrayList<String> Role = null;
}
