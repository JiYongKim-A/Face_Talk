package zoom.meeting.domain.repositoryImpl.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcMessageRepository implements MessageRepository {
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public JdbcMessageRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }


    @Override
    public Message send(Message message) {
        String sql = "insert into messageRepository(sender,recipient,date,title,message,isRead) values(?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getRecipient());
            pstmt.setString(3, message.getDate());
            pstmt.setString(4, message.getTitle());
            pstmt.setString(5, message.getMessage());
            pstmt.setString(6, message.getIsRead());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                message.setManageSeq(rs.getLong(1));
            }
            return message;
        } catch (SQLException e) {
            log.error("message send 실패 ,message 정보 = [{}]", message.getDate(), message.getSender());
            throw exTranslator.translate("send", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Message> findByNickNameAll(String nickName) {

        String sql = "select * from messageRepository where recipient = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickName);

            rs = pstmt.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                Message message = new Message(
                        rs.getString("sender"),
                        rs.getString("recipient"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("isRead"));

                message.setManageSeq(rs.getLong("manageSeq"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw exTranslator.translate("findByNickNameAll", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Message> findByManageSeq(long manageSeq) {

        String sql = "select * from messageRepository where manageSeq = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, manageSeq);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Message message = new Message(
                        rs.getString("sender"),
                        rs.getString("recipient"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("isRead"));
                message.setManageSeq(rs.getLong("manageSeq"));
                return Optional.of(message);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw exTranslator.translate("findByManageSeq", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }

    }

    @Override
    public void removeByManageSeq(long manageSeq) {

        String sql = "delete from messageRepository where manageSeq=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, manageSeq);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("{}번: meassege의 delete 실패", manageSeq);
            throw exTranslator.translate("removeByManageSeq", sql, e);
        } finally {
            close(conn, pstmt, rs);
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
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "Y");
            pstmt.setLong(2, manageSeq);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("{} : message isRead update 실패 {}", manageSeq);
            throw exTranslator.translate("isReadUpdate", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

}
