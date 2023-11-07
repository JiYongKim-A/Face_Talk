package zoom.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

@RequiredArgsConstructor
public class InitData {
    private final MemberRepository memberRepository;
    private final NoteRepository noteRepository;
    private final MessageRepository messageRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

       // init Member
        Member firstMem = new Member("kim", "kim", "지용", "화끈이");
        memberRepository.save(firstMem);
        Member secondMem = new Member("ji","ji","쿙","불닭");
        memberRepository.save(secondMem);

        //init Note
        Note note = new Note("123123123-231231231","koki124","2021-09-22", "컴공 회의방", "지용","오늘은 어려웠다.");
        noteRepository.save(note);

        //init Message
        Message message = new Message("kim", "테스트용이야", "2020/04/05", "hi", "how are you?","N");
        // 기본 isRead 세팅 N으로 보내기
        message.setIsRead("N");
        messageRepository.send(message);
    }
}

