package net.bytemc.evelon.repository;

import net.bytemc.evelon.annotations.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class RepositoryClass<T> {

    private final Class<T> clazz;
    private final RepositoryField[] fields;

    public RepositoryClass(@NotNull Class<T> clazz) {
        this.clazz = clazz;
        this.fields = Arrays.stream(clazz.getDeclaredFields())
                .map(field -> {
                    if(field.isAnnotationPresent(PrimaryKey.class)) {
                        return new PrimaryRepositoryField(field);
                    }
                    return new RepositoryField(field);
                }).toArray(value -> new RepositoryField[0]);
    }

    public Class<?> getOrigin() {
        return this.clazz;
    }

    public RepositoryField[] getFields() {
        return fields;
    }

    public RepositoryField[] getPrimary() {
        return fields;
    }


    public boolean hasField(String id) {
        return Arrays.stream(fields).anyMatch(field -> field.getName().equals(id));
    }

    public RepositoryField getField(String id) {
        return Arrays.stream(fields).filter(field -> field.getName().equals(id)).findFirst().orElseThrow();
    }



}