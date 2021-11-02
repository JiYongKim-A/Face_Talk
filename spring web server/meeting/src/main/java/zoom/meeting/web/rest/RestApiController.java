package zoom.meeting.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryImpl.MemoryNoteRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.web.rest.jsonData.JsonData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*")
@Controller
@RequiredArgsConstructor
public class RestApiController {

    private final NoteRepository noteRepository;

    @PostMapping("/saveData")
    @ResponseBody
    public String rest(@RequestBody JsonData jsonData) throws IOException {

        Optional<Note> findNote = noteRepository.findByUserUUID(jsonData.getUserUUID());

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);


        // text, title 없을 시 저장 안함
        if(jsonData.getTitle().equals("")&&jsonData.getText().equals("")){
            return "fail";
        }
        if(jsonData.getTitle().equals("")){
            jsonData.setTitle("제목 없음");
        }


        // 이미 문서함에 저장된 형식이 없을시 저장
        if(findNote.isEmpty()){
            noteRepository.save(new Note(
                    jsonData.getUserUUID(),
                    jsonData.getRoomUUID().split("-")[2],
                    time,
                    jsonData.getTitle(),
                    jsonData.getNickName(),
                    jsonData.getText()));
            log.info("저장시킴");
            log.info("jsonData ={}",jsonData);
            return "ok";
        }
    // 저장된 문서가 있을시 업데이트
        Note note = findNote.get();
        note.setDate(time);
        note.setTitle(jsonData.getTitle());
        note.setText(jsonData.getText());
        log.info("업뎃 시킴");
        log.info("jsonData ={}",jsonData);
    return "ok";
    }

}
