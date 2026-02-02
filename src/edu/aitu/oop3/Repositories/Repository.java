package edu.aitu.oop3.Repositories;

import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> findById(Connection con, ID id) throws SQLException;
}
