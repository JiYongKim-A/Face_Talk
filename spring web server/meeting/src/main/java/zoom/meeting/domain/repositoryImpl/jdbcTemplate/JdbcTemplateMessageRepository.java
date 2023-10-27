package zoom.meeting.domain.repositoryImpl.jdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Repository
public class JdbcTemplateMessageRepository implements MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMessageRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Message send(Message message) {
        String sql = "insert into messageRepository(sender,recipient,date,title,message,isRead) values(?,?,?,?,?,?)";
        if (!message.getRecipient().equals("") && message.getRecipient() != null) {
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, message.getSender());
                pstmt.setString(2, message.getRecipient());
                pstmt.setString(3, message.getDate());
                pstmt.setString(4, message.getTitle());
                pstmt.setString(5, message.getMessage());
                pstmt.setString(6, message.getIsRead());
                return pstmt;
            }, generatedKeyHolder);
            message.setManageSeq(generatedKeyHolder.getKey().longValue());
            return message;
        } else {
            return null;
        }
    }

    @Override
    public List<Message> findByNickNameAll(String nickName) {

        String sql = "select * from messageRepository where recipient = ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    return new Message(
                            rs.getLong("manageSeq"),
                            rs.getString("sender"),
                            rs.getString("recipient"),
                            rs.getString("date"),
                            rs.getString("title"),
                            rs.getString("message"),
                            rs.getString("isRead"));
                }
                , nickName);

    }

    @Override
    public Optional<Message> findByManageSeq(long manageSeq) {
        String sql = "select * from messageRepository where manageSeq = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, messageRowMapper(), manageSeq));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeByManageSeq(long manageSeq) {

        String sql = "delete from messageRepository where manageSeq=?";
        if (findByManageSeq(manageSeq).isPresent()) {
            jdbcTemplate.update(sql, manageSeq);
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
        String sql = "update messageRepository set isRead=? where manageSeq=?";
        if (findByManageSeq(manageSeq).isPresent()) {
            jdbcTemplate.update(sql, "Y", manageSeq);
        }
    }

    private RowMapper<Message> messageRowMapper() {
        return (rs, rowNum) -> new Message(rs.getLong("manageSeq"),
                rs.getString("sender"),
                rs.getString("recipient"),
                rs.getString("date"),
                rs.getString("title"),
                rs.getString("message"),
                rs.getString("isRead"));
    }
}
