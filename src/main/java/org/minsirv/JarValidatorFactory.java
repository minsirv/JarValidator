package org.minsirv;

public class JarValidatorFactory {
    public static JarValidator createJarValidator () {
        return new JarValidatorImpl();
    }
}
