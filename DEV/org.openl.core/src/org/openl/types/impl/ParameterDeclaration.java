package org.openl.types.impl;

import java.util.Objects;

import org.openl.source.IOpenSourceCodeModule;
import org.openl.types.IOpenClass;
import org.openl.types.IParameterDeclaration;
import org.openl.util.ClassUtils;

/**
 * @author snshor
 *
 */
public class ParameterDeclaration implements IParameterDeclaration {

    private final IOpenClass type;
    private final String name;
    private IOpenSourceCodeModule sourceCode;

    public ParameterDeclaration(IOpenClass type, String name) {
        this(type, name, null);
    }

    public ParameterDeclaration(IOpenClass type, String name, IOpenSourceCodeModule sourceCode) {
        this.type = type;
        this.name = name;
        this.sourceCode = sourceCode;
    }

    @Override
    public String getDisplayName(int mode) {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IOpenClass getType() {
        return type;
    }

    @Override
    public IOpenSourceCodeModule getModule() {
        return sourceCode;
    }

    @Override
    public void removeDebugInformation() {
        sourceCode = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParameterDeclaration)) {
            return false;
        }
        ParameterDeclaration paramDecl = (ParameterDeclaration) obj;

        return Objects.equals(name, paramDecl.name) && Objects.equals(type, paramDecl.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return ClassUtils.getShortClassName(type.getInstanceClass()) + " " + name;
    }
}
