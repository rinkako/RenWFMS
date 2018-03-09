package org.sysu.renCommon;

public class GlobalConfigContext {
    /**
     * Salt for authority encryption.
     */
    public static final String AUTHORITY_SALT = "bab53e0679c74ren8148b75ea2a7db4e";

    /**
     * Size for engine log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 0;

    public static void TestHello() {
        System.out.println("hello!");
    }
}
