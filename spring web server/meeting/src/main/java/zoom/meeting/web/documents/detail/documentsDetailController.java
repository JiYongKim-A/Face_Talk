package zoom.meeting.web.documents.detail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class documentsDetailController {

    private final NoteRepository noteRepository;

    // Documents detail 조회
    @GetMapping("myDocuments/detail/{manageSeq}")
    public String getDetail(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                            @PathVariable Long manageSeq, Model model) {

        Optional<Note> findNote = noteRepository.findByManageSeq(manageSeq);
        if(findNote.isEmpty()){
            return "redirect:/myDocuments";
        }
        Note note = findNote.get();
        if(!form.getNickName().equals(note.getNickName())){
            return "redirect:/myDocuments";
        }

        //success logic
        model.addAttribute("note", note);

        return "/documents/detail/detailForm";
    }


    //Documents detail 수정
    @GetMapping("myDocuments/detail/modify/{manageSeq}")
    public String modifyForm(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                             @PathVariable Long manageSeq,
                             Model model) {

        Optional<Note> findNote = noteRepository.findByManageSeq(manageSeq);
        if (findNote.isEmpty()) {
            return "redirect:/myDocuments";
        }
        Note note = findNote.get();
        if (!note.getNickName().equals(form.getNickName())) {
            return "redirect:/myDocuments";
        }
        //success logic
        model.addAttribute("note", note);
        return "/documents/detail/detailModifyForm";

    }

    @PostMapping("myDocuments/detail/modify/{manageSeq}")
    public String modify(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                         @PathVariable Long manageSeq,
                         @Valid @ModelAttribute("note")Note modifyForm, BindingResult bindingResult) {

        Optional<Note> findNote = noteRepository.findByManageSeq(manageSeq);
        if (findNote.isEmpty()) {

            return "redirect:/myDocuments";
        }
        Note note = findNote.get();
        if (!note.getNickName().equals(form.getNickName())) {
            return "redirect:/myDocuments";
        }

        if(bindingResult.hasErrors()){
            modifyForm.setNickName(note.getNickName());
            modifyForm.setDate(note.getDate());
            modifyForm.setRoomUUID(note.getRoomUUID());
            return "documents/detail/detailModifyForm";
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);

        //success logic
        note.setTitle(modifyForm.getTitle());
        note.setText(modifyForm.getText());
        note.setDate(time);

        log.info("note = {}",note);
        log.info("note  UUID= {},",note.getUserUUID(),note.getRoomUUID());

        noteRepository.updateByManageSeq(note.getManageSeq(),note);

        return "redirect:/myDocuments";
    }


    //Documents detail 삭제
    @PostMapping("myDocuments/detail/remove/{manageSeq}")
    public String removeDetail(@SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY) SessionForm form,
                               @PathVariable Long manageSeq) {

        if(!form.getNickName().equals(noteRepository.findByManageSeq(manageSeq).get().getNickName())){
            return"/documents/myDocuments" ;
        }
        noteRepository.removeByManageSeq(manageSeq);
        return"redirect:/myDocuments";
    }




}
