package org.sysu.renResourcing.context;

import org.sysu.renResourcing.basic.RIdentifier;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage : Session context for Token split item storing
 */
public class RSession extends RIdentifier implements Serializable, RCacheablesContext {
    public String Username;

    public String BeginTimestamp;

    public String EndTimestamp;

    public String IssuingAuthority;
}
