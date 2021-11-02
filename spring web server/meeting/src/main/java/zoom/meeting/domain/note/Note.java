package zoom.meeting.domain.note;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Data
@Getter @Setter
public class Note {
    /**
     * ==정리노트==
     *
     * 관리 번호
     * 작성자
     * 날짜
     * 제목
     * 정리 내용
     * content ( 파일...etc)
     */

    //primary
    private Long manageSeq;

    //primary
    private String nickName;

    private String date;

    @NotEmpty
    private String title;

    private String text;

    private String userUUID;

    private String roomUUID;

    private Object content; // Content 확장 가능


    public Note(String userUUID, String roomUUID, String date, String title, String nickName, String text) {
        this.userUUID = userUUID;
        this.roomUUID = roomUUID;
        this.nickName = nickName;
        this.date = date;
        this.title = title;
        this.text = text;
    }
}
