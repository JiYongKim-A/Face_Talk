package zoom.meeting.web.signUp.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpForm {


    @NotBlank
    private String name;

    @NotBlank
    private String nickName;


    @NotBlank
    private String loginId;


    @NotBlank
    private String password;
}
