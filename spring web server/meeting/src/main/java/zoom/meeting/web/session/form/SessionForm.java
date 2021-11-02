package zoom.meeting.web.session.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data @Getter @Setter
public class SessionForm {

    private String loginId;

    private String nickName;

}
