package zoom.meeting.web.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;
import zoom.meeting.service.message.MessageService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MessageService messageService;

    @GetMapping("/")
    public String home(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY, required = false) SessionForm sessionForm,
            Model model) {
        if (sessionForm == null) {
            return "home";
        }

        model.addAttribute("sessionForm", sessionForm);
        model.addAttribute("check", messageService.checkNewMessage(sessionForm.getNickName()));
        log.info("check= {}", messageService.checkNewMessage(sessionForm.getNickName()));
        return "loggedHome";
    }

}
