package com.javapex.core.type.classreading;

import com.javapex.asm.SpringAsmInfo;
import com.javapex.core.annotation.AnnotationAttributes;
import org.objectweb.asm.AnnotationVisitor;

import java.util.Map;

final class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    private final String annotationType;

    private final Map<String, AnnotationAttributes> attributesMap;

    AnnotationAttributes attributes = new AnnotationAttributes();


    public AnnotationAttributesReadingVisitor(
            String annotationType, Map<String, AnnotationAttributes> attributesMap) {
        super(SpringAsmInfo.ASM_VERSION);

        this.annotationType = annotationType;
        this.attributesMap = attributesMap;

    }
    @Override
    public final void visitEnd(){
        this.attributesMap.put(this.annotationType, this.attributes);
    }

    public void visit(String attributeName, Object attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }


}
