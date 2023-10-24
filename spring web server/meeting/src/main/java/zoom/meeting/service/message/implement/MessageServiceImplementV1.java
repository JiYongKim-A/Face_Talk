package zoom.meeting.service.message.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.service.message.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementV1 implements MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public List<Message> getMessageList(String nickName) {
        return messageRepository.findByNickNameAll(nickName);
    }

    @Transactional
    @Override
    public Message readMessage(long messageManageSeq, String recipientNickName) {
        Optional<Message> findMessage = messageRepository.findByManageSeq(messageManageSeq);
        if (findMessage.isPresent() && findMessage.get().getRecipient().equals(recipientNickName)) {
            messageRepository.isReadUpdate(messageManageSeq);
            return findMessage.get();
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public void removeMessage(long messageManageSeq, String recipientNickName) {
        Optional<Message> findMessage = messageRepository.findByManageSeq(messageManageSeq);
        if (findMessage.isPresent() && findMessage.get().getRecipient().equals(recipientNickName)) {
            messageRepository.removeByManageSeq(messageManageSeq);
        }
    }

    @Transactional
    @Override
    public List<String> sendMessage(String recipient, String sender, String title, String message) {
        recipient = recipient.replaceAll(" ", "");
        String[] recipientList = recipient.split(",");
        List<String> nickNameCheckList = new ArrayList<>();

        for (String nickName : recipientList) {
            if (!memberRepository.allNickName().contains(nickName)) {
                nickNameCheckList.add(nickName);
            }
        }
        if (!nickNameCheckList.isEmpty()) {
            return nickNameCheckList;
        }
        for (String rec : recipientList) {
            Message m = new Message(sender, rec, getTime(), title, message, "N");
            messageRepository.send(m);
        }
        return null;
    }

    @Transactional
    @Override
    public String checkNewMessage(String nickName) {
        return messageRepository.checkNewMessage(nickName);
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
