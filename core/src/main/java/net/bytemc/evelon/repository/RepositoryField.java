package net.bytemc.evelon.repository;

import net.bytemc.evelon.annotations.RowName;
import net.bytemc.evelon.misc.EvelonReflections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;

public class RepositoryField {

    private final String name;
    private final Field field;

    public RepositoryField(@NotNull Field field) {
        if (field.isAnnotationPresent(RowName.class)) {
            this.name = field.getDeclaredAnnotation(RowName.class).name();
        } else {
            this.name = field.getName();
        }
        this.field = field;
    }

    public String getName() {
        return this.name;
    }

    @Contract(pure = true)
    public @NotNull Class<?> type() {
        return this.field.getType();
    }

    public Object getValue(Object parent) {
        return EvelonReflections.getFieldValue(this.field, parent);
    }
}
