package zoom.meeting.web.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.web.session.form.SessionForm;
import zoom.meeting.web.sessionConst.SessionConst;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MessageRepository messageRepository;

    @GetMapping("/")
    public String home(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY,required = false)SessionForm sessionForm,
            Model model) {

        if(sessionForm == null){
            return "home";
        }

        model.addAttribute("sessionForm", sessionForm);
        model.addAttribute("check", messageRepository.checkNewMessage(sessionForm.getNickName()));
        log.info("check= {}",messageRepository.checkNewMessage(sessionForm.getNickName()));
        return "loggedHome";
    }

}
