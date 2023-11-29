package net.bytemc.evelon.repository.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytemc.evelon.misc.Reflections;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.bytemc.evelon.repository.RepositoryHelper;
import net.bytemc.evelon.repository.properties.StartupProperties;

import java.lang.reflect.Field;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public abstract class RepositoryHandler<K, V> {

    protected final Repository<V> repository;

    public RepositoryHandler(Class<V> clazz, StartupProperties<V> startupProperties) {
        this(Repository.create(clazz, startupProperties));
    }

    public RepositoryHandler(Class<V> clazz) {
        this(Repository.create(clazz, StartupProperties.empty()));
    }

    public abstract V get(K k);

    public void update(V v) {
        repository.query().chronological().upsert(v);
    }

    public void delete(V v) {
        var primaries = repository.repositoryClass().getPrimaries();
        if (primaries.isEmpty()) {
            throw new UnsupportedOperationException(
                "The repository " + repository.repositoryClass().clazz().getName() + " has no primary keys"
            );
        }
        var deletion = repository.query();
        for (Field primary : primaries) {
            deletion.filter(Filter.match(RepositoryHelper.getFieldName(primary), Reflections.readField(v, primary)));
        }
        deletion.delete();
    }

    public V getOrCreate(K k, Supplier<V> creation) {
        V query = get(k);
        if (query != null) {
            return query;
        }
        query = creation.get();
        update(query);
        return query;
    }

}
