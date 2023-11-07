package zoom.meeting.domain.message;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@RequiredArgsConstructor
public class Message {

    private long manageSeq; // 관리 번호

    private String sender; //보낸는이
    private String recipient; // 받는이
    private String date; // 보낸 날짜

    private String title; // 메세지 제목
    private String message; // 메세지 내용
    private String isRead; // 메세지 열람 여부  true: 열람 , false:미열람

    public Message(String sender, String recipient, String date, String title, String message, String isRead) {
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
    }

    public Message(Long manageSeq,String sender, String recipient, String date, String title, String message, String isRead) {
        this.manageSeq = manageSeq;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
    }
}
