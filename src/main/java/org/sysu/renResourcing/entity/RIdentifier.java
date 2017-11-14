package org.sysu.renResourcing.entity;
import org.hibernate.loader.custom.Return;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage : A abstract class for the entity which can be an
 *         identifier and can be stored to steady memory.
 */
public abstract class RIdentifier implements Serializable {

    public String Id = null;

    public String GetIdWithType() {
        return String.format("%s?%s", this.getClass().getName(), this.Id);
    }

    public String ToXML() {
        return String.format("<RIdentifier>%s</RIdentifier>", this.GetIdWithType());
    }

    public static final String IndentPaddingUnitSpace = "  ";
}
