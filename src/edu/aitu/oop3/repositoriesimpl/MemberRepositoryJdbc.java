package edu.aitu.oop3.repositoriesimpl;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Entities.Member;
import edu.aitu.oop3.Repositories.MemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryJdbc implements MemberRepository {

    @Override
    public Optional<Member> findById(Connection con, long id) throws SQLException {
        String sql = "SELECT id, full_name, phone FROM members WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Member(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("phone")
                ));
            }
        }
    }

    @Override
    public Optional<Member> findById(Connection con, Long aLong) throws SQLException {
        return findById(con, aLong.longValue());
    }

    @Override
    public Member save(Member entity) {
        return null;
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }

    @Override
    public List<Book> listAvailableBooks() {
        return List.of();
    }
}
