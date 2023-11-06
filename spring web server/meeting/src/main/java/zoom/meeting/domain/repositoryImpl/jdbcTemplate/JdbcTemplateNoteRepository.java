package zoom.meeting.domain.repositoryImpl.jdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcTemplateNoteRepository implements NoteRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateNoteRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Note save(Note note) {
        String sql = "insert into noteRepository (nickName, date, title, content, userUUID, roomUUID) values(?,?,?,?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, note.getNickName());
            pstmt.setString(2, note.getDate());
            pstmt.setString(3, note.getTitle());
            pstmt.setString(4, note.getText());
            pstmt.setString(5, note.getUserUUID());
            pstmt.setString(6, note.getRoomUUID());
            return pstmt;
        }, generatedKeyHolder);
        note.setManageSeq(generatedKeyHolder.getKey().longValue());
        return note;
    }

    @Override
    public List<Note> findAll() {
        String sql = "select * from noteRepository";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Note(
                rs.getLong("manageSeq"),
                rs.getString("userUUID"),
                rs.getString("roomUUID"),
                rs.getString("content"),
                rs.getString("title"),
                rs.getString("nickName"),
                rs.getString("text")
        ));
    }

    @Override
    public List<Note> findByNickNameAll(String nickName) {

        String sql = "select * from noteRepository where nickName = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    return new Note(
                            rs.getLong("manageSeq"),
                            rs.getString("userUUID"),
                            rs.getString("roomUUID"),
                            rs.getString("date"),
                            rs.getString("title"),
                            rs.getString("nickName"),
                            rs.getString("content"));
                }, nickName);
    }

    @Override
    public Optional<Note> findByUserUUID(String userUUID) {
        String sql = "select * from noteRepository where userUUID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, noteRowMapper(), userUUID));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<Note> findByManageSeq(Long manageSeq) {
        String sql = "select * from noteRepository where manageSeq = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, noteRowMapper(), manageSeq));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public Note updateByManageSeq(Long manageSeq, Note updatedNote) {

        String sql = "update noteRepository set nickName=?,date=?,title=?,content=?,userUUID=?,roomUUID=? where manageSeq=?";
        if (findByManageSeq(manageSeq).isPresent()) {
            jdbcTemplate.update(sql, updatedNote.getNickName(), updatedNote.getDate(), updatedNote.getTitle(), updatedNote.getText(), updatedNote.getUserUUID(), updatedNote.getRoomUUID(), manageSeq);
            return updatedNote;
        } else {
            return null;
        }
    }

    @Override
    public void removeByManageSeq(Long manageSeq) {

        String sql = "delete from noteRepository where manageSeq=?";
        if (findByManageSeq(manageSeq).isPresent()) {
            jdbcTemplate.update(sql, manageSeq);
        }
    }

    private RowMapper<Note> noteRowMapper() {
        return ((rs, rowNum) -> new Note(
                rs.getLong("manageSeq"),
                rs.getString("userUUID"),
                rs.getString("roomUUID"),
                rs.getString("date"),
                rs.getString("title"),
                rs.getString("nickName"),
                rs.getString("content")));
    }
}
