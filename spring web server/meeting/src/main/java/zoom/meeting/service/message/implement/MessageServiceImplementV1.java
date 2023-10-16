package zoom.meeting.service.message.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
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
public class MessageServiceImplementV1 implements MessageService {

    private final TransactionTemplate txTemplate;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageServiceImplementV1(MessageRepository messageRepository, MemberRepository memberRepository, PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Message> getMessageList(String nickName) {
        return txTemplate.execute(status -> {
            try {
                return messageRepository.findByNickNameAll(nickName);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public Message readMessage(long messageManageSeq, String recipientNickName) {
        return txTemplate.execute(status -> {
            try {
                Optional<Message> findMessage = messageRepository.findByManageSeq(messageManageSeq);
                if (findMessage.isPresent() && findMessage.get().getRecipient().equals(recipientNickName)) {
                    messageRepository.isReadUpdate(messageManageSeq);
                    return findMessage.get();
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void removeMessage(long messageManageSeq, String recipientNickName) {
        txTemplate.executeWithoutResult(status -> {
            try {
                Optional<Message> findMessage = messageRepository.findByManageSeq(messageManageSeq);
                if (findMessage.isPresent() && findMessage.get().getRecipient().equals(recipientNickName)) {
                    messageRepository.removeByManageSeq(messageManageSeq);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public List<String> sendMessage(String recipient, String sender, String title, String message) {
        recipient = recipient.replaceAll(" ", "");
        String[] recipientList = recipient.split(",");
        List<String> nickNameCheckList = new ArrayList<>();
        return txTemplate.execute(status -> {
            try {
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
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public String checkNewMessage(String nickName) {
        return txTemplate.execute(status -> {
            try {
                return messageRepository.checkNewMessage(nickName);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }

}
