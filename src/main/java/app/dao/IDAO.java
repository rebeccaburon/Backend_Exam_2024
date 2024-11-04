package app.dao;

import java.util.List;

public interface IDAO<T> {

    T create(T t);

    List<T> getAll();

    T getById(Long id);


    T update(T t);

    void delete(Long id);
}
