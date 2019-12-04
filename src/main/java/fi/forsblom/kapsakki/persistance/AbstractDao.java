package fi.forsblom.kapsakki.persistance;

import java.util.Collection;
import java.util.Optional;

public interface AbstractDao<T> {
	Optional<T> findByIdAndOwnerId(int id, String ownerId);
    Collection<T> findAllByOwnerId(String ownerId);
    T save(T t);
    T updateById(T t);
    int deleteById(int id);
}
