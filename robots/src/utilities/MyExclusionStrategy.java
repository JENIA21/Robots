package utilities;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Arrays;
import java.util.Set;

class MyExclusionStrategy implements ExclusionStrategy {
    private Set<String> classesToIgnore = Set.of(
            "javax.swing.JComponent",
            "javax.swing.JPanel",
            "javax.swing.JFrame");

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes field) {
        return field.getDeclaringClass().getName().startsWith("javax")
                || field.getDeclaringClass().getName().startsWith("java.awt.");
    }
}