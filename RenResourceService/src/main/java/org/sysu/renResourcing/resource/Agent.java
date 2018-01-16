package org.sysu.renResourcing.resource;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2017/12/15
 * Usage :
 */
public class Agent extends RResource {
    /**
     * Construct a new Human entity.
     */
    public Agent() {
        this.Role = new ArrayList<>();
    }

    /**
     * Add a role to this agent.
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
     * Remove a role from this agent.
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
     * agent name, unique.
     */
    public String Name = "";

    /**
     * agent binding url.
     */
    public String Url = "";

    /**
     * A list of role which this agent has.
     */
    protected ArrayList<String> Role = null;
}
