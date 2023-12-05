package net.bytemc.evelon.local;

import net.bytemc.evelon.layers.RepositoryLayer;
import net.bytemc.evelon.misc.SortedOrder;
import net.bytemc.evelon.query.DataQuery;
import net.bytemc.evelon.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public final class LocalStorageLayer implements RepositoryLayer {

    @Override
    public <T> void create(@NotNull DataQuery<T> query, T value) {
        query.getRepository().localStorage().add(LocalStorageEntry.of(value));
    }

    @Override
    public <T> void createIfNotExists(DataQuery<T> query, T value) {
        if (!this.exists(query)) {
            this.create(query, value);
        }
    }

    @Override
    public <T> void deleteAll(DataQuery<T> query) {
        this.applyFilters(query).forEach(t -> query.getRepository().localStorage().removeIf(entry -> entry.value().equals(t)));
    }

    @Override
    public <T> void delete(DataQuery<T> query, T value) {
        query.getRepository().localStorage().remove(value);
    }

    @Override
    public <T> void update(DataQuery<T> query, T value) {
        // nothing to do because the same memory reference is used
    }

    @Override
    public <T> void upsert(DataQuery<T> query, T value) {
        // we can use the same method because the same memory reference is used and update not used
        this.createIfNotExists(query, value);
    }

    @Override
    public <T> List<T> findAll(DataQuery<T> query) {
        return this.applyFilters(query).toList();
    }

    @Override
    public <T> T find(DataQuery<T> query) {
        return this.applyFilters(query).findAny().orElse(null);
    }

    @Override
    public <T> boolean exists(DataQuery<T> query) {
        return this.applyFilters(query).findAny().isPresent();
    }

    @Override
    public <T> long count(DataQuery<T> query) {
        return this.applyFilters(query).count();
    }

    @Override
    public <T> long sum(DataQuery<T> query, String id) {
        //TODO
        return 0;
    }

    @Override
    public <T> double avg(DataQuery<T> query, String id) {
        //TODO
        return 0;
    }

    @Override
    public <T> List<T> order(DataQuery<T> query, String id, int max, SortedOrder order) {
        //TODO
        return null;
    }

    private <T> Stream<T> applyFilters(DataQuery<T> query) {
        return query.getRepository().localStorage().stream().map(LocalStorageEntry::value);
    }
}
