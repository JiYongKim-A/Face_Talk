package zoom.meeting.repository.springBootTest.messageRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("send Test")
    void sendTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");

        //when
        Message sendMessage = messageRepository.send(message);
        //then
        assertThat(messageRepository.findByManageSeq(sendMessage.getManageSeq()).get())
                .isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("findByNickNameAll Test")
    void findByNickNameAllTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");
        //when
        Message sendMessage = messageRepository.send(message);
        //then
        List<Message> byNickNameAll = messageRepository.findByNickNameAll(t2.getNickName());
        for (Message m : byNickNameAll) {
            log.info("message = {}", m);
        }
        assertThat(messageRepository.findByNickNameAll(t2.getNickName())).contains(message);

    }

    @Test
    @DisplayName("findByManageSeq Test")
    void findByManageSeqTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");
        //when
        Message sendMessage = messageRepository.send(message);
        //then
        assertThat(messageRepository.findByManageSeq(sendMessage.getManageSeq()).get().getManageSeq()).isEqualTo(sendMessage.getManageSeq());
    }

    @Test
    @DisplayName("removeByManageSeq Test")
    void removeByManageSeqTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");
        //when
        Message sendMessage = messageRepository.send(message);
        messageRepository.removeByManageSeq(sendMessage.getManageSeq());
        //then
        assertThat(messageRepository.findByManageSeq(sendMessage.getManageSeq()))
                .isEmpty();
    }

    @Test
    @DisplayName("checkNewMessage Test")
    void checkNewMessageTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");
        //when
        Message sendMessage = messageRepository.send(message);
        //then
        assertThat(messageRepository.checkNewMessage(t2.getNickName())).isEqualTo("Y");
    }

    @Test
    @DisplayName("isReadUpdate Test")
    void isReadUpdateTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member t2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(t1.getNickName(), t2.getNickName(), getTime(), "test", "test", "N");
        //when
        Message sendMessage = messageRepository.send(message);
        messageRepository.isReadUpdate(sendMessage.getManageSeq());
        //then
        assertThat(messageRepository.checkNewMessage(t2.getNickName())).isEqualTo("N");
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
