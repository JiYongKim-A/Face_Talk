package zoom.meeting.service.documentService.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.service.documentService.DocumentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentServiceImplementV1 implements DocumentService {

    private final NoteRepository noteRepository;
    @Override
    public List<Note> findAllDocumentsByNickName(String nickName) {
        return noteRepository.findByNickNameAll(nickName);
    }

    @Override
    public Note saveDocument(String userUUID, String roomUUID, String title, String nickName, String text){
        if(title.equals("") && text.equals("")){
            return null;
        }
        if(title.equals("")){
            title = "제목 없음";
        }
        Optional<Note> findNote = noteRepository.findByUserUUID(userUUID);
        if (findNote.isEmpty()) {
            return noteRepository.save(new Note(
                    userUUID,
                    roomUUID,
                    getTime(),
                    title,
                    nickName,
                    text));
        }else{
            Note note = findNote.get();
            note.setDate(getTime());
            note.setTitle(title);
            note.setText(text);
            return findNote.get();
        }
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
