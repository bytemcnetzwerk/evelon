package net.bytemc.evelon.api.repository.clazz;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.bytemc.evelon.api.annotations.PrimaryKey;
import net.bytemc.evelon.api.repository.RepositoryClass;
import net.bytemc.evelon.api.repository.RepositoryField;
import net.bytemc.evelon.api.repository.field.PrimaryRepositoryFieldImpl;
import net.bytemc.evelon.api.repository.field.RepositoryFieldImpl;
import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class RepositoryClassImpl implements RepositoryClass {

    private final Class<?> clazz;
    private final RepositoryField[] fields;
    private final RepositoryField[] primaryFields;

    public RepositoryClassImpl(Class<?> clazz) {
        this.clazz = clazz;

        this.fields = Arrays.stream(clazz.getDeclaredFields())
                .map(field -> field.isAnnotationPresent(PrimaryKey.class) ? new PrimaryRepositoryFieldImpl(field, this) : new RepositoryFieldImpl(field, this))
                .toArray(RepositoryField[]::new);

        this.primaryFields = Arrays.stream(fields)
                .filter(repositoryField -> repositoryField instanceof PrimaryRepositoryFieldImpl)
                .toArray(RepositoryField[]::new);
    }
}
