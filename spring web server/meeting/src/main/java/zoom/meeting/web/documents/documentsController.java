package zoom.meeting.web.documents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;
import zoom.meeting.service.documentService.DocumentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class documentsController {

    private final DocumentService documentService;

    @GetMapping("/myDocuments")
    public String myDocuments(
            @SessionAttribute(name = SessionConst.LOGIN_SESSION_KEY,required = false) SessionForm form,
            Model model) {
        model.addAttribute("notes",documentService.findAllDocumentsByNickName(form.getNickName()));
        return"/documents/myDocuments" ;
    }

}
