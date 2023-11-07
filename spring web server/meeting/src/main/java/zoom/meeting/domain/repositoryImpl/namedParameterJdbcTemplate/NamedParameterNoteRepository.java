package zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class NamedParameterNoteRepository implements NoteRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public NamedParameterNoteRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("noteRepository")
                .usingGeneratedKeyColumns("manageSeq");
    }

    @Override
    public Note save(Note note) {
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("nickName", note.getNickName())
                .addValue("date", note.getDate())
                .addValue("title", note.getTitle())
                .addValue("content", note.getText())
                .addValue("userUUID", note.getUserUUID())
                .addValue("roomUUID", note.getRoomUUID());
        Number key = jdbcInsert.executeAndReturnKey(param);
        note.setManageSeq(key.longValue());
        return note;
    }

    @Override
    public List<Note> findAll() {
        String sql = "select * from noteRepository";
        return jdbcTemplate.query(sql, noteRowMapper());
    }

    @Override
    public List<Note> findByNickNameAll(String nickName) {

        String sql = "select * from noteRepository where nickName = :nickName";
        Map<String, Object> param = Map.of("nickName", nickName);
        return jdbcTemplate.query(sql, param, noteRowMapper());
    }

    @Override
    public Optional<Note> findByUserUUID(String userUUID) {
        String sql = "select * from noteRepository where userUUID = :userUUID";
        try {
            Map<String, Object> param = Map.of("userUUID", userUUID);
            return Optional.of(jdbcTemplate.queryForObject(sql, param, noteRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<Note> findByManageSeq(Long manageSeq) {
        String sql = "select * from noteRepository where manageSeq = :manageSeq";
        try {
            Map<String, Object> param = Map.of("manageSeq", manageSeq);
            return Optional.of(jdbcTemplate.queryForObject(sql, param, noteRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public Note updateByManageSeq(Long manageSeq, Note updatedNote) {
        String sql = "update noteRepository set nickName=:nickName,date=:date,title=:title,content=:content,userUUID=:userUUID,roomUUID=:roomUUID where manageSeq=:manageSeq";
        if (findByManageSeq(manageSeq).isPresent()) {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("nickName", updatedNote.getNickName())
                    .addValue("date", updatedNote.getDate())
                    .addValue("title", updatedNote.getTitle())
                    .addValue("content", updatedNote.getText())
                    .addValue("userUUID", updatedNote.getUserUUID())
                    .addValue("roomUUID", updatedNote.getRoomUUID())
                    .addValue("manageSeq", manageSeq);
            jdbcTemplate.update(sql, param);
            return findByManageSeq(manageSeq).get();
        } else {
            return null;
        }
    }

    @Override
    public void removeByManageSeq(Long manageSeq) {
        String sql = "delete from noteRepository where manageSeq=:manageSeq";
        if (findByManageSeq(manageSeq).isPresent()) {
            Map<String, Object> param = Map.of("manageSeq", manageSeq);
            jdbcTemplate.update(sql, param);
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
