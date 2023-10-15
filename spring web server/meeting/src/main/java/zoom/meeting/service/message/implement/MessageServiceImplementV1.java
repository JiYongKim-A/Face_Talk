package zoom.meeting.service.message.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public List<Message> getMyMessageList(String nickName) {
        return messageRepository.findByNickNameAll(nickName);
    }

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

    @Override
    public void removeMessage(long messageManageSeq, String recipientNickName) {
        Optional<Message> findMessage = messageRepository.findByManageSeq(messageManageSeq);
        if (findMessage.isPresent() && findMessage.get().getRecipient().equals(recipientNickName)) {
            messageRepository.removeByManageSeq(messageManageSeq);
        }
    }

    @Override
    public List<String> sendMessage(String recipient, String sender, String title, String message) {
        // 1. 공백 제거
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

        //success logic
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);

        for (String rec : recipientList) {
            Message m = new Message(sender, rec, time, title, message, "N");
            messageRepository.send(m);
        }
        return null;
    }

}
