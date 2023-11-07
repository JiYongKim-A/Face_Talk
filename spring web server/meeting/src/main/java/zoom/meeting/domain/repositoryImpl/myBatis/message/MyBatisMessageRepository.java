package zoom.meeting.domain.repositoryImpl.myBatis.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMessageRepository implements MessageRepository {

    private final MessageRepositoryMapper messageRepositoryMapper;

    @Override
    public Message send(Message message) {
        messageRepositoryMapper.send(message);
        return message;
    }

    @Override
    public List<Message> findByNickNameAll(String nickName) {
        return messageRepositoryMapper.findByNickNameAll(nickName);
    }

    @Override
    public Optional<Message> findByManageSeq(long manageSeq) {
        return messageRepositoryMapper.findByManageSeq(manageSeq);
    }

    @Override
    public void removeByManageSeq(long manageSeq) {
        messageRepositoryMapper.removeByManageSeq(manageSeq);
    }

    @Override
    public String checkNewMessage(String nickName) {
        List<Message> findList = findByNickNameAll(nickName);
        if (findList == null) {
            return "N";
        }

        Optional<Message> check = findList.stream()
                .filter(m -> m.getIsRead().equals("N"))
                .findFirst();
        if (check.isEmpty()) {
            return "N";
        }
        return "Y";
    }

    @Override
    public void isReadUpdate(long manageSeq) {
        messageRepositoryMapper.isReadUpdate(manageSeq);
    }
}
