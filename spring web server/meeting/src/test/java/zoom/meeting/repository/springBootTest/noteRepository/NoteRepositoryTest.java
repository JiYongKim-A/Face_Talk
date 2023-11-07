package zoom.meeting.repository.springBootTest.noteRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MemberRepository memberRepository;

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

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
