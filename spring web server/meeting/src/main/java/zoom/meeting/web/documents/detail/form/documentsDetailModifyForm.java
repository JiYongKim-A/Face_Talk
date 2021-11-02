package zoom.meeting.web.documents.detail.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class documentsDetailModifyForm {


    @NotEmpty
    private String title;


    private String text;

}
