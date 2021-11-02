package zoom.meeting.domain.repositoryInterface;

import zoom.meeting.domain.message.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    // 발송
    Message send(Message message);

    // 닉네임으로 모든 메세지 조회
    List<Message> findByNickNameAll(String nickName);

    // 관리번호로 메세지 조회
    Optional<Message> findByManageSeq(long manageSeq);

    //관리번호로 메세지 삭제
    void removeByManageSeq(long manageSeq);

    //  닉네임으로 새로운 메세지가 있는지 없는지 확인
    String checkNewMessage(String nickName);


    default void isReadUpdate(long manageSeq){ };
    default void clearMessageStore() { };

}
