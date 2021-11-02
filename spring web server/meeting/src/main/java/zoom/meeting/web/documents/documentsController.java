package zoom.meeting.web.documents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.web.session.form.SessionForm;
import zoom.meeting.web.sessionConst.SessionConst;

@Controller
@RequiredArgsConstructor
public class documentsController {

    private final NoteRepository noteRepository;

    @GetMapping("/myDocuments")
    public String myDocuments(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY,required = false) SessionForm form,
            Model model) {

        // 모든 노트들 다 모델에 넣어서 보냄
        model.addAttribute("notes",noteRepository.findByNickNameAll(form.getNickName()));


        return"/documents/myDocuments" ;
    }

}
