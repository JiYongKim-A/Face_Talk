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
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class NamedParameterMessageRepository implements MessageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public NamedParameterMessageRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("messageRepository")
                .usingGeneratedKeyColumns("manageSeq");
    }

    @Override
    public Message send(Message message) {
        if (!message.getRecipient().equals("") && message.getRecipient() != null) {
            SqlParameterSource param = new BeanPropertySqlParameterSource(message);
            Number key = jdbcInsert.executeAndReturnKey(param);
            message.setManageSeq(key.longValue());
            return message;
        } else {
            return null;
        }
    }

    @Override
    public List<Message> findByNickNameAll(String nickName) {
        String sql = "select * from messageRepository where recipient = :recipient";
        Map<String, Object> param = Map.of("recipient", nickName);
        return jdbcTemplate.query(sql, param, messageRowMapper());
    }

    @Override
    public Optional<Message> findByManageSeq(long manageSeq) {
        String sql = "select * from messageRepository where manageSeq = :manageSeq";
        try {
            Map<String, Object> param = Map.of("manageSeq", manageSeq);
            return Optional.of(jdbcTemplate.queryForObject(sql, param, messageRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeByManageSeq(long manageSeq) {
        String sql = "delete from messageRepository where manageSeq = :manageSeq";
        if (findByManageSeq(manageSeq).isPresent()) {
            Map<String, Object> param = Map.of("manageSeq", manageSeq);
            jdbcTemplate.update(sql, param);
        }
    }

    @Override
    public String checkNewMessage(String nickName) {
        List<Message> findList = findByNickNameAll(nickName);
        if (findList == null) {
            return "N";
        }

        Optional<Message> check = findList.stream()
                .filter(m -> m.getIsRead().equals("N"))
                .findFirst();
        if (check.isEmpty()) {
            return "N";
        }
        return "Y";
    }

    @Override
    public void isReadUpdate(long manageSeq) {
        String sql = "update messageRepository set isRead=:isRead where manageSeq=:manageSeq";
        if (findByManageSeq(manageSeq).isPresent()) {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("isRead", "Y")
                    .addValue("manageSeq", manageSeq);
            jdbcTemplate.update(sql, param);
        }
    }

    private RowMapper<Message> messageRowMapper() {
        return BeanPropertyRowMapper.newInstance(Message.class);
    }
}
