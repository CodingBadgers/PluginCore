package uk.codingbadgers.plugincore.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(NotSerialized.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
