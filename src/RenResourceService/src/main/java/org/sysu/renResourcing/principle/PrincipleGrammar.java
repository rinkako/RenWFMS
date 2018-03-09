/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.principle;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage : Interface for principle descriptor grammar parser.
 */
public interface PrincipleGrammar {
    /**
     * Parse a principle descriptor to a principle object.
     * @param descriptor principle descriptor string
     * @return parsed principle object, null if parse failure
     */
    RPrinciple Parse(String descriptor);
}
