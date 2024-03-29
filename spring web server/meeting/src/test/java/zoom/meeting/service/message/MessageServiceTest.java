package zoom.meeting.service.message;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

@Slf4j
@Transactional
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;


    @Test
    @DisplayName("MessageServiceImplementV1 Send Test 1")
    public void MessageServiceSendTest1() {
        //given
        Member mem1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member mem2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));
        Message message = new Message(mem1.getNickName(), mem2.getNickName(), getTime(), "test", "test", "N");

        //when
        List<String> check = messageService.sendMessage(mem2.getNickName(), mem1.getNickName(), "test", "test");
        List<Message> messageList = messageRepository.findByNickNameAll(mem2.getNickName());
        long manageSeq = messageList.get(0).getManageSeq();
        message.setManageSeq(manageSeq);

        //then
        Assertions.assertThat(check).isNull();
        Assertions.assertThat(messageRepository.findByNickNameAll(mem2.getNickName())).contains(message);
    }

    @Test
    @DisplayName("MessageServiceImplementV1 Send Test 2")
    public void MessageServiceSendTest2() {
        //given
        Member mem2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));

        //when
        String exceptionName = "TestingNickName";
        List<String> check = messageService.sendMessage(exceptionName, mem2.getNickName(), "test", "test");

        //then
        Assertions.assertThat(check).isNotNull();
        Assertions.assertThat(check).contains(exceptionName);
    }

    @Test
    @DisplayName("MessageServiceImplementV1 check Test")
    public void MessageServiceCheckTest() {
        //given
        Member mem1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member mem2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));

        //when
        List<String> check = messageService.sendMessage(mem2.getNickName(), mem1.getNickName(), "test", "test");
        List<Message> messageList = messageRepository.findByNickNameAll(mem2.getNickName());
        Message message = messageList.get(0);

        //then
        Assertions.assertThat(check).isNull();
        Assertions.assertThat(messageService.checkNewMessage(mem2.getNickName())).isEqualTo("Y");

    }

    @Test
    @DisplayName("MessageServiceImplementV1 read Test")
    public void MessageServiceReadTest() {
        //given
        Member mem1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member mem2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));

        //when
        List<String> check = messageService.sendMessage(mem2.getNickName(), mem1.getNickName(), "test", "test");
        List<Message> messageList = messageRepository.findByNickNameAll(mem2.getNickName());
        Message message = messageList.get(0);

        //then
        Assertions.assertThat(check).isNull();
        Assertions.assertThat(message.getIsRead()).isEqualTo("N");
        messageService.readMessage(message.getManageSeq(), message.getRecipient());
        Assertions.assertThat(messageRepository.findByManageSeq(message.getManageSeq()).get().getIsRead()).isEqualTo("Y");
    }

    @Test
    @DisplayName("MessageServiceImplementV1 remove Test")
    public void MessageServiceRemoveTest() {
        //given
        Member mem1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        Member mem2 = memberRepository.save(new Member("T2", "T2", "T2", "T2"));

        //when
        List<String> check = messageService.sendMessage(mem2.getNickName(), mem1.getNickName(), "test", "test");
        List<Message> messageList = messageRepository.findByNickNameAll(mem2.getNickName());
        Message message = messageList.get(0);
        messageService.removeMessage(message.getManageSeq(), message.getRecipient());

        //then
        Assertions.assertThat(messageRepository.findByManageSeq(message.getManageSeq())).isEmpty();
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
