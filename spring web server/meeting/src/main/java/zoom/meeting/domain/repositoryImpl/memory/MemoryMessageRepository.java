package zoom.meeting.domain.repositoryImpl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MessageRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MemoryMessageRepository implements MessageRepository {

    private static Map<Long, Message> store = new ConcurrentHashMap<>();
    private static long manageSequence=0L;

    @Override
    public Message send(Message message) {
        message.setManageSeq(manageSequence++);
        store.put(message.getManageSeq(),message);
        return message;
    }

    @Override
    public String checkNewMessage(String nickName) {
        List<Message> findList = findByNickNameAll(nickName);
        if(findList==null){
            return "Y";
        }

        Optional<Message> check = findList.stream()
                .filter(m -> m.getIsRead().equals("N"))
                .findFirst();
        if(check.isEmpty()){
            //  모두 열람
            return "Y";
        }
        // 열람 안한 메세지가 있음
        return "N";
    }

    @Override
    public List<Message> findByNickNameAll(String nickName) {
        return store.values().stream()
                .filter(m -> m.getRecipient().equals(nickName))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Message> findByManageSeq(long manageSeq) {

        return Optional.of(store.get(manageSeq));
    }

    @Override
    public void removeByManageSeq(long manageSeq) {
        Optional<Message> findMessage = findByManageSeq(manageSeq);
        if(findMessage.isEmpty()){
            log.error("삭제할 결과가 없습니다 manageSeq={}",manageSeq);
        }
        store.remove(findMessage.get().getManageSeq());
    }

    @Override
    public void clearMessageStore() {
        store.clear();
    }

    @Override
    public void isReadUpdate(long manageSeq) {
        Optional<Message> byManageSeq = findByManageSeq(manageSeq);
        if(byManageSeq.isEmpty()){
            log.error("존재하지 않는 message입니다 manageSeq={}",manageSeq);
        }

        Message message = byManageSeq.get();
        message.setIsRead("Y");
    }
}
