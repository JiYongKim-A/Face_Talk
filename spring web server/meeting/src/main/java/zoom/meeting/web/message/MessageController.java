package zoom.meeting.web.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import zoom.meeting.domain.message.Message;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.web.message.form.SendMessageForm;
import zoom.meeting.web.session.form.SessionForm;
import zoom.meeting.web.sessionConst.SessionConst;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @GetMapping("myMessage")
    public String myMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY)SessionForm form,
                            Model model) {

        String nickName = form.getNickName();
        List<Message> messageList = messageRepository.findByNickNameAll(nickName);

        model.addAttribute("messages", messageList);

        return "message/messageList";
    }


    @GetMapping("myMessage/readMessage/{manageSeq}")
    public String readMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY)SessionForm form,
                              @PathVariable("manageSeq")Long manageSeq,
                              Model model) {
        Optional<Message> findMessage = messageRepository.findByManageSeq(manageSeq);
        if(findMessage.isEmpty()){
            return "redirect:/myMessage";
        }
        Message message = findMessage.get();
        if(!message.getRecipient().equals(form.getNickName())){
            return "redirect:/myMessage";
        }

        //success logic

        messageRepository.isReadUpdate(manageSeq);


        model.addAttribute("message", message);

        return "message/readMessage";
    }

    @GetMapping("myMessage/removeMessage/{manageSeq}")
    public String removeMessage(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY)SessionForm form,
                                @PathVariable("manageSeq")Long manageSeq){

        Optional<Message> findMessage = messageRepository.findByManageSeq(manageSeq);
        if(findMessage.isEmpty()){
            return "redirect:/myMessage";
        }
        Message message = findMessage.get();
        if(!message.getRecipient().equals(form.getNickName())){
            return "redirect:/myMessage";
        }

        //success logic
        messageRepository.removeByManageSeq(manageSeq);

        return "redirect:/myMessage";
    }

    // send Message part
    @GetMapping("myMessage/sendMessage")
    public String sendMessage(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY)SessionForm sessionForm,
            @ModelAttribute("sendMessageForm")SendMessageForm messageForm,
            Model model) {

        model.addAttribute("sender", sessionForm.getNickName());
        model.addAttribute("recipient",null);

        return "message/sendMessage/sendMessageForm";
    }

    @PostMapping("myMessage/sendMessage")
    public String sending(@RequestParam("sender")String sender,
            @Valid @ModelAttribute("sendMessageForm")SendMessageForm messageForm,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            log.info("FieldError = {}",bindingResult);
            model.addAttribute("sender",sender);
            return "message/sendMessage/sendMessageForm";
        }

        // recipient check logic
        String recipients = messageForm.getRecipient();
        log.info("공백 제거전 = {}",recipients);
       recipients = recipients.replaceAll(" ", "");

        log.info("공백 제거 후 = {}",recipients);
        List<String> recipientList = Arrays.asList(recipients.split(","));

        List<String> checkList = new ArrayList<>(); // 체크용
        for (String s : recipientList) {
            checkList.add(s);
        }

        List<String> nickNames = memberRepository.allNickName();
        checkList.removeAll(nickNames);
        log.info("체크리스트 = {}",checkList.toString());

        if(!checkList.isEmpty()){
            log.error("없는 사용자에게 발송 = {}", checkList.toString());
            bindingResult.addError(new FieldError("messageForm","recipient",messageForm.getRecipient(),false,null,new Object[]{checkList},"존재하지 않는 닉네임 입니다 : {0}"));
            model.addAttribute("sender",sender);
            return "message/sendMessage/sendMessageForm";
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);

        //success
        for(int i=0; i< recipientList.size();i++ ){

            Message message = new Message(sender, recipientList.get(i), time, messageForm.getTitle(), messageForm.getMessage(),"N");
            messageRepository.send(message);
        }

        return "redirect:/myMessage";
    }

    @PostMapping("myMessage/sendMessage/{recipient}")
    public String sendingWithParam(@RequestParam("sender")String sender,
                          @Valid @ModelAttribute("sendMessageForm")SendMessageForm messageForm,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            log.info("FieldError = {}",bindingResult);
            model.addAttribute("sender",sender);
            return "message/sendMessage/sendMessageForm";
        }

        // recipient check logic
        String recipients = messageForm.getRecipient();
        log.info("공백 제거전 = {}",recipients);
        recipients = recipients.replaceAll(" ", "");

        log.info("공백 제거 후 = {}",recipients);
        List<String> recipientList = Arrays.asList(recipients.split(","));

        List<String> checkList = new ArrayList<>(); // 체크용
        for (String s : recipientList) {
            checkList.add(s);
        }

        List<String> nickNames = memberRepository.allNickName();
        checkList.removeAll(nickNames);
        log.info("체크리스트 = {}",checkList.toString());

        if(!checkList.isEmpty()){
            log.error("없는 사용자에게 발송 = {}", checkList.toString());
            bindingResult.addError(new FieldError("messageForm","recipient",messageForm.getRecipient(),false,null,new Object[]{checkList},"존재하지 않는 닉네임 입니다 : {0}"));
            model.addAttribute("sender",sender);
            return "message/sendMessage/sendMessageForm";
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);

        //success
        for(int i=0; i< recipientList.size();i++ ){

            Message message = new Message(sender, recipientList.get(i), time, messageForm.getTitle(), messageForm.getMessage(),"N");
            messageRepository.send(message);
        }

        return "redirect:/myMessage";
    }



    @GetMapping("myMessage/sendMessage/{recipient}")
    public String sendMessageWithParam(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY)SessionForm sessionForm,
            @ModelAttribute("sendMessageForm")SendMessageForm messageForm,
            @PathVariable("recipient") String recipient,
            Model model) {

        model.addAttribute("sender", sessionForm.getNickName());
        model.addAttribute("recipient", recipient);

        return "message/sendMessage/sendMessageForm";
    }



}
