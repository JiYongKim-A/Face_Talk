package zoom.meeting.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zoom.meeting.domain.member.Member;
import zoom.meeting.service.login.LoginService;
import zoom.meeting.web.login.form.LoginForm;
import zoom.meeting.web.session.form.SessionForm;
import zoom.meeting.web.sessionConst.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm")LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest req,
                        @RequestParam(defaultValue = "/") String redirectURL) {

        if(bindingResult.hasErrors()){
            log.info("FieldError = {}",bindingResult);
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            log.info("Invalid login attempt id=[{}] pw=[{}]",form.getLoginId(), form.getPassword());
            bindingResult.reject("loginFail","잘못된 ID 혹은 PW 입니다.");
            bindingResult.reject("tryAgain");
            return "login/loginForm";
        }

        // success logic -> mk Session

//        HttpSession session = req.getSession();
//        SessionForm loggedMemSession = new SessionForm(loginMember.getLoginId(), loginMember.getNickName());
//        session.setAttribute(SessionConst.LOGIN_SESSION_KEY, loggedMemSession);
        loginService.generateSession(req,loginMember.getLoginId(),loginMember.getNickName());

        return "redirect:"+redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }


}
