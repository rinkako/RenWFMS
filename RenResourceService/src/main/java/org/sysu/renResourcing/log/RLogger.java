package org.sysu.renResourcing.log;

/**
 * Author: Rinkako
 * Date  : 2017/11/20 0020
 * Usage :
 */
public interface RLogger {
    boolean Echo(REvent e);

    boolean Echo(String s);

    boolean Log(REvent e);

    boolean Log(String s);

    boolean LogEcho(REvent e);

    boolean LogEcho(String s);

    boolean LogException(String exStr);

    boolean LogException(Exception ex);
}
