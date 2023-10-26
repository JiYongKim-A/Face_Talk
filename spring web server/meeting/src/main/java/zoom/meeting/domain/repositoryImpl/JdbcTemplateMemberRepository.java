package zoom.meeting.domain.repositoryImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Primary
@Repository
public class JdbcTemplateMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String> allLoginId() {
        String sql = "select loginId from memberRepository";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("loginId"));
    }


    @Override
    public List<String> allNickName() {
        String sql = "select nickName from memberRepository";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("nickName"));
    }


    @Override
    public Member save(Member member) {
        String sql = "insert into memberRepository (loginId,password,name,nickName) values(?,?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getLoginId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getNickName());
            return pstmt;
        }, generatedKeyHolder);
        member.setManageSeq(generatedKeyHolder.getKey().longValue());
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from memberRepository where loginId = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper(), loginId));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<Member> findAll() {

        String sql = "select * from memberRepository";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Member(rs.getString("loginId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("nickName")));
    }

    @Override
    public Member updateByLoginId(String loginId, Member updatedMember) {

        String sql = "update memberRepository set loginId=?,password=?,name=?,nickName=? where loginId=?";
        if (findByLoginId(loginId).isPresent()) {
            jdbcTemplate.update(sql, updatedMember.getLoginId(), updatedMember.getPassword(), updatedMember.getName(), updatedMember.getNickName(), loginId);
            return updatedMember;
        } else {
            return null;
        }
    }

    @Override
    public void removeByLoginId(String loginId) {
        String sql = "delete from memberRepository where loginId=?";
        if (findByLoginId(loginId).isPresent()) {
            jdbcTemplate.update(sql, loginId);
        }
    }

    @Override
    public Optional<Member> findByManageSeq(Long manageSeq) {
        String sql = "select * from memberRepository where manageSeq = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper(), manageSeq));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(rs.getLong("manageSeq"),
                rs.getString("loginId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("nickName"));
    }
}