package zoom.meeting.web.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import zoom.meeting.domain.message.Message;
import zoom.meeting.service.message.MessageService;
import zoom.meeting.web.message.form.SendMessageForm;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("myMessage")
    public String myMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                            Model model) {
        List<Message> messageList = messageService.getMessageList(form.getNickName());
        model.addAttribute("messages", messageList);
        return "message/messageList";
    }


    @GetMapping("myMessage/readMessage/{manageSeq}")
    public String readMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                              @PathVariable("manageSeq") Long manageSeq,
                              Model model) {

        Message message = messageService.readMessage(manageSeq, form.getNickName());
        if (message == null) {
            return "redirect:/myMessage";
        }
        model.addAttribute("message", message);
        return "message/readMessage";
    }

    @GetMapping("myMessage/removeMessage/{manageSeq}")
    public String removeMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                                @PathVariable("manageSeq") Long manageSeq) {
        messageService.removeMessage(manageSeq,form.getNickName());
        return "redirect:/myMessage";
    }

    @GetMapping("myMessage/sendMessage")
    public String sendMessage(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm sessionForm,
            @ModelAttribute("sendMessageForm") SendMessageForm messageForm,
            Model model) {
        model.addAttribute("sender", sessionForm.getNickName());
        model.addAttribute("recipient", null);
        return "message/sendMessage/sendMessageForm";
    }

    @PostMapping("myMessage/sendMessage")
    public String sending(@RequestParam("sender") String sender,
                          @Valid @ModelAttribute("sendMessageForm") SendMessageForm messageForm,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            log.info("FieldError = {}", bindingResult);
            model.addAttribute("sender", sender);
            return "message/sendMessage/sendMessageForm";
        }

        List<String> checkList = messageService.sendMessage(messageForm.getRecipient(), sender, messageForm.getTitle(), messageForm.getMessage());
        if (checkList != null) {
            log.info("없는 사용자에게 발송 = {}", checkList);
            bindingResult.addError(new FieldError("messageForm", "recipient", messageForm.getRecipient(), false, null, new Object[]{checkList}, "존재하지 않는 닉네임 입니다 : {0}"));
            model.addAttribute("sender", sender);
            return "message/sendMessage/sendMessageForm";
        }
        return "redirect:/myMessage";
    }

    // 간편한 답장하기 버튼을 통한 메시지 전송
    @PostMapping("myMessage/sendMessage/{recipient}")
    public String sendingWithParam(@RequestParam("sender") String sender,
                                   @Valid @ModelAttribute("sendMessageForm") SendMessageForm messageForm,
                                   BindingResult bindingResult,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            log.info("FieldError = {}", bindingResult);
            model.addAttribute("sender", sender);
            return "message/sendMessage/sendMessageForm";
        }

        List<String> checkList = messageService.sendMessage(messageForm.getRecipient(), sender, messageForm.getTitle(), messageForm.getMessage());
        if (checkList!= null) {
            log.info("없는 사용자에게 발송 = {}", checkList);
            bindingResult.addError(new FieldError("messageForm", "recipient", messageForm.getRecipient(), false, null, new Object[]{checkList}, "존재하지 않는 닉네임 입니다 : {0}"));
            model.addAttribute("sender", sender);
            return "message/sendMessage/sendMessageForm";
        }
        return "redirect:/myMessage";
    }

    @GetMapping("myMessage/sendMessage/{recipient}")
    public String sendMessageWithParam(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm sessionForm,
            @ModelAttribute("sendMessageForm") SendMessageForm messageForm,
            @PathVariable("recipient") String recipient,
            Model model) {
        model.addAttribute("sender", sessionForm.getNickName());
        model.addAttribute("recipient", recipient);
        return "message/sendMessage/sendMessageForm";
    }
}
