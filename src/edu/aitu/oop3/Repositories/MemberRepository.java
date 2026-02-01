package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Optional<Member> findById(Connection con, long id) throws SQLException;
}
