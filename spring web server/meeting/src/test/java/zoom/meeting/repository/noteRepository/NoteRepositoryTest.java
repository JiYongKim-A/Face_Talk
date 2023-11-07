package zoom.meeting.repository.noteRepository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate.NamedParameterMemberRepository;
import zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate.NamedParameterNoteRepository;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static zoom.meeting.ConnectionConstForTest.*;

@Slf4j
public class NoteRepositoryTest {
    NoteRepository noteRepository;
    MemberRepository memberRepository;
    long noteManageSeq;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setPoolName("myPool");
        dataSource.setMaximumPoolSize(10);
        noteRepository = new NamedParameterNoteRepository(dataSource);
        memberRepository = new NamedParameterMemberRepository(dataSource);
    }

    @Test
    @DisplayName("save Test")
    void saveTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = note.getManageSeq();
        Note findNote = noteRepository.findByManageSeq(note.getManageSeq()).get();
        //then
        assertThat(note).isEqualTo(findNote);

    }


    @Test
    @DisplayName("findByNickNameAll Test")
    void findByNickNameAllTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = note.getManageSeq();
        List<Note> noteList = noteRepository.findByNickNameAll(t1.getNickName());
        //then
        assertThat(noteList).contains(note);

    }

    @Test
    @DisplayName("findByUserUUID Test")
    void findByUserUUIDTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = note.getManageSeq();
        Note findNote = noteRepository.findByUserUUID(userUUID).get();
        //then
        assertThat(note).isEqualTo(findNote);
    }

    @Test
    @DisplayName("findByManageSeq Test")
    void findByManageSeqTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = note.getManageSeq();
        Note findNote = noteRepository.findByManageSeq(note.getManageSeq()).get();
        //then
        assertThat(note).isEqualTo(findNote);
    }

    @Test
    @DisplayName("updateByManageSeq Test")
    void updateByManageSeqTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = note.getManageSeq();
        noteRepository.updateByManageSeq(note.getManageSeq(), new Note(userUUID, roomUUID, getTime(), "test2", t1.getNickName(), "test2"));
        Note findNote = noteRepository.findByManageSeq(note.getManageSeq()).get();
        //then
        assertThat(note).isNotEqualTo(findNote);
    }

    @Test
    @DisplayName("removeByManageSeq Test")
    void removeByManageSeqTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        String userUUID = UUID.randomUUID().toString();
        String roomUUID = UUID.randomUUID().toString();

        //when
        Note note = noteRepository.save(new Note(userUUID, roomUUID, getTime(), "test", t1.getNickName(), "test"));
        noteManageSeq = -1;
        noteRepository.removeByManageSeq(note.getManageSeq());
        //then
        assertThat(noteRepository.findByManageSeq(note.getManageSeq())).isEmpty();
    }

    @AfterEach
    void afterEach() {
        memberRepository.removeByLoginId("T1");
        if (noteManageSeq != -1) {
            noteRepository.removeByManageSeq(noteManageSeq);
        }
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
