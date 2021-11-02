package zoom.meeting.domain.repositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class JdbcNoteRepository implements NoteRepository {

    private final DataSource dataSource;

    @Override
    public Note save(Note note) {
        String sql = "insert into noteRepository (nickName, date, title, content, userUUID, roomUUID) values(?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, note.getNickName());
            pstmt.setString(2, note.getDate());
            pstmt.setString(3, note.getTitle());
            pstmt.setString(4, note.getText());
            pstmt.setString(5, note.getUserUUID());
            pstmt.setString(6, note.getRoomUUID());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                note.setManageSeq(rs.getLong(1));
            } else {
                throw new SQLException("note 조회 실패");
            }
            return note;
        } catch (Exception e) {
            log.error("note save 실패 ,note 정보 = [{}]", note.getDate(),note.getNickName(),note.getTitle());
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public List<Note> findAll() {

        String sql = "select * from noteRepository";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Note> notes = new ArrayList<>();
            while (rs.next()) {
                Note note = new Note(
                        rs.getString("userUUID"),
                        rs.getString("roomUUID"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getString("content"));
                note.setManageSeq(rs.getLong("manageSeq"));
                notes.add(note);
            }
            return notes;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public List<Note> findByNickNameAll(String nickName) {

        String sql = "select * from noteRepository where nickName = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,nickName);

            rs = pstmt.executeQuery();

            List<Note> notes = new ArrayList<>();
            while(rs.next()) {
                Note note = new Note(
                        rs.getString("userUUID"),
                        rs.getString("roomUUID"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getString("content"));
                    note.setManageSeq(rs.getLong("manageSeq"));
                notes.add(note);
            }
            return notes;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Note> findByUserUUID(String userUUID) {
        String sql = "select * from noteRepository where userUUID = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,userUUID);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Note note = new Note(
                        rs.getString("userUUID"),
                        rs.getString("roomUUID"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getString("content"));
                note.setManageSeq(rs.getLong("manageSeq"));
                return Optional.of(note);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Note> findByManageSeq(Long manageSeq) {
        String sql = "select * from noteRepository where manageSeq = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,manageSeq);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Note note = new Note(
                        rs.getString("userUUID"),
                        rs.getString("roomUUID"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getString("content"));
                note.setManageSeq(rs.getLong("manageSeq"));
                return Optional.of(note);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }


    @Override
    public Note updateByManageSeq(Long manageSeq, Note updatedNote) {


        String sql = "update noteRepository set nickName=?,date=?,title=?,content=?,userUUID=?,roomUUID=? where manageSeq=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updatedNote.getNickName());
            pstmt.setString(2, updatedNote.getDate());
            pstmt.setString(3, updatedNote.getTitle());
            pstmt.setString(4, updatedNote.getText());
            pstmt.setString(5, updatedNote.getUserUUID());
            pstmt.setString(6, updatedNote.getRoomUUID());
            pstmt.setLong(7, manageSeq);

//            rs = pstmt.executeQuery();
            pstmt.executeUpdate();

            return updatedNote;

        } catch (Exception e) {
            log.error("{} : note의 update 실패",updatedNote.getDate(),updatedNote.getNickName(),updatedNote.getTitle());
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public void removeByManageSeq(Long manageSeq) {


        String sql = "delete from noteRepository where manageSeq=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, manageSeq);

           // delete 는 excuteUpdate임 rs 안받음
            pstmt.executeUpdate(); //

//            rs = pstmt.executeQuery();

        } catch (Exception e) {
            log.error("{}번: note의 delete 실패",manageSeq);
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }

    }


    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                close(conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

}
