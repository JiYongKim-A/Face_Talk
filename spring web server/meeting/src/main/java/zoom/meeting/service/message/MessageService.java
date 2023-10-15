package zoom.meeting.service.message;

import zoom.meeting.domain.message.Message;

import java.util.List;

public interface MessageService {
    // 메시지 리스트 반환
    List<Message> getMessageList(String nickName);
    // 메시지 읽기
    Message readMessage(long messageManageSeq, String recipientNickName);
    // 메시지 삭제
    void removeMessage(long messageManageSeq, String recipientNickName);
    // 메시지 보내기
    List<String> sendMessage(String recipient,String sender,String title, String message);

    String checkNewMessage(String nickName);
}
