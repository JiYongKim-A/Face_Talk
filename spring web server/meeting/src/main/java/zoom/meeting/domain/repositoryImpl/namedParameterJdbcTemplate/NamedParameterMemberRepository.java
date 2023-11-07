package zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class NamedParameterMemberRepository implements MemberRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public NamedParameterMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("memberRepository")
                .usingGeneratedKeyColumns("manageSeq");
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
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setManageSeq(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from memberRepository where loginId =:loginId";
        try {
            Map<String, Object> param = Map.of("loginId", loginId);
            Member member = jdbcTemplate.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from memberRepository";
        return jdbcTemplate.query(sql, memberRowMapper());
    }

    @Override
    public Member updateByLoginId(String loginId, Member updatedMember) {

        String sql = "update memberRepository set loginId = :loginId, password=:password,name=:name,nickName=:nickName where loginId=:updateLoginId";
        if (findByLoginId(loginId).isPresent()) {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("loginId", updatedMember.getLoginId())
                    .addValue("password", updatedMember.getPassword())
                    .addValue("name", updatedMember.getName())
                    .addValue("nickName", updatedMember.getNickName())
                    .addValue("updateLoginId", loginId);

            jdbcTemplate.update(sql, param);
            return updatedMember;
        } else {
            return null;
        }
    }

    // 현재 removeBylogiId 진행중!!!
    @Override
    public void removeByLoginId(String loginId) {
        String sql = "delete from memberRepository where loginId=:loginId";
        if (findByLoginId(loginId).isPresent()) {
            Map<String, Object> param = Map.of("loginId", loginId);
            jdbcTemplate.update(sql, param);
        }
    }

    @Override
    public Optional<Member> findByManageSeq(Long manageSeq) {
        String sql = "select * from memberRepository where manageSeq = :manageSeq";
        try {
            Map<String, Object> param = Map.of("manageSeq", manageSeq);
            return Optional.of(jdbcTemplate.queryForObject(sql, param, memberRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}