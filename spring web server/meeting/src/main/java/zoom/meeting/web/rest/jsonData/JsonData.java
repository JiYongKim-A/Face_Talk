package zoom.meeting.web.rest.jsonData;

import lombok.Data;

@Data
public class JsonData {
    private String nickName;
    private String roomUUID;
    private String userUUID;
    private String title;
    private String text;

}
