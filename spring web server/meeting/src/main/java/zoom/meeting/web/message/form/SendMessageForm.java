package zoom.meeting.web.message.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SendMessageForm {


    @NotEmpty
    private String title;

    private String message;

    @NotEmpty
    private String recipient;

}
