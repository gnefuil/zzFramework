package com.javapex.cglib;

import net.sf.cglib.core.DefaultNamingPolicy;

/**
 * Custom extension of CGLIB's {@link DefaultNamingPolicy}, modifying
 * the tag in generated class names from "ByCGLIB" to "BySpringCGLIB".
 *
 * <p>This is primarily designed to avoid clashes between a regular CGLIB
 * version (used by some other library) and Spring's embedded variant,
 * in case the same class happens to get proxied for different purposes.
 *
 * @author Juergen Hoeller
 * @since 3.2.8
 */
public class SpringNamingPolicy extends DefaultNamingPolicy {

    public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();

    @Override
    protected String getTag() {
        return "BySpringCGLIB";
    }

}
