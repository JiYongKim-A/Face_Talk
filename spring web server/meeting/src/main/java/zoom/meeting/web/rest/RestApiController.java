package zoom.meeting.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zoom.meeting.domain.note.Note;
import zoom.meeting.service.documentService.DocumentService;
import zoom.meeting.web.rest.jsonData.JsonData;

@Slf4j
@CrossOrigin(origins = "*")
@Controller
@RequiredArgsConstructor
public class RestApiController {

    private final DocumentService documentService;

    @PostMapping("/saveData")
    @ResponseBody
    public String rest(@RequestBody JsonData jsonData) {
        Note note = documentService.saveDocument(jsonData.getUserUUID(),
                jsonData.getRoomUUID().split("-")[2],
                jsonData.getTitle(),
                jsonData.getNickName(),
                jsonData.getText());
        if(note == null){
            log.info("fail");
            return "fail";
        }
        log.info("fail");
        return "ok";
    }
}
