package zoom.meeting.domain.repositoryImpl.myBatis.message;

import org.apache.ibatis.annotations.Mapper;
import zoom.meeting.domain.message.Message;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MessageRepositoryMapper {

    // 발송
    void send(Message message);

    // 닉네임으로 모든 메세지 조회
    List<Message> findByNickNameAll(String nickName);

    // 관리번호로 메세지 조회
    Optional<Message> findByManageSeq(long manageSeq);

    //관리번호로 메세지 삭제
    void removeByManageSeq(long manageSeq);

    //  닉네임으로 새로운 메세지가 있는지 없는지 확인
    String checkNewMessage(String nickName);

    void isReadUpdate(long manageSeq);

}
