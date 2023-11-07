package zoom.meeting.repository.functionTest.noteRepository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate.NamedParameterMemberRepository;
import zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate.NamedParameterNoteRepository;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.repository.functionTest.repositoryConnectionConst.ConnectionConstForTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class NoteRepositoryTest {
    NoteRepository noteRepository;
    MemberRepository memberRepository;
    TransactionStatus status;
    PlatformTransactionManager txManager;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConstForTest.URL);
        dataSource.setUsername(ConnectionConstForTest.USERNAME);
        dataSource.setPassword(ConnectionConstForTest.PASSWORD);
        dataSource.setPoolName("myPool");
        dataSource.setMaximumPoolSize(10);
        noteRepository = new NamedParameterNoteRepository(dataSource);
        memberRepository = new NamedParameterMemberRepository(dataSource);
        txManager = new DataSourceTransactionManager(dataSource);
        this.status = txManager.getTransaction(new DefaultTransactionDefinition());
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
        noteRepository.removeByManageSeq(note.getManageSeq());
        //then
        assertThat(noteRepository.findByManageSeq(note.getManageSeq())).isEmpty();
    }

    @AfterEach
    void afterEach() {
        txManager.rollback(status);
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
