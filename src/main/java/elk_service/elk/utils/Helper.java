package elk_service.elk.utils;

import java.lang.reflect.Field;

public class Helper {
    public static void copyNonNullProperties(Object source, Object target) throws IllegalAccessException {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            Object value = sourceField.get(source);
            if (value != null) {
                try {
                    Field targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);

                    if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                        targetField.set(target, value);
                    }
                } catch (NoSuchFieldException e) {
                    continue;
                }
            }
        }
    }
}
