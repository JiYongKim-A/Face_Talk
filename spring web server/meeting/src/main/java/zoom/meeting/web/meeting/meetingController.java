package zoom.meeting.web.meeting;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zoom.meeting.domain.member.Member;
import zoom.meeting.web.session.form.SessionForm;
import zoom.meeting.web.sessionConst.SessionConst;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
public class meetingController {

    private final MessageSource messageSource;

    @GetMapping("/start")
    public String start(RedirectAttributes redirectAttributes,
                        HttpServletResponse response,
                        @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY,required = false)SessionForm form) {
        String url = messageSource.getMessage("urlPath", null, null);
        String nickName = form.getNickName();

        redirectAttributes.addAttribute("urlPath", url);

        try {
            Cookie nickNameCookie = new Cookie("nickName", URLEncoder.encode(nickName, "UTF-8"));
            response.addCookie(nickNameCookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "redirect:https://{urlPath}";
    }

    @PostMapping("/join")
    public String join(RedirectAttributes redirectAttributes,
                       HttpServletResponse response,
                       @RequestParam String code,
                       @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY,required = false)SessionForm form) {
        Pattern pattern = Pattern.compile("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        Matcher matcher = pattern.matcher(code);

        if(!matcher.matches()){
            return "redirect:/";
        }

        if(code.equals("")){
            return "redirect:/";
        }

        // properties에서 url값 가져옴
        String url = messageSource.getMessage("urlPath", null, null);
        String nickName = form.getNickName();

        redirectAttributes.addAttribute("urlPath", url);
        redirectAttributes.addAttribute("code", code);

        try {
            Cookie nickNameCookie = new Cookie("nickName", URLEncoder.encode(nickName, "UTF-8"));
            response.addCookie(nickNameCookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "redirect:https://{urlPath}/{code}";
    }


}
