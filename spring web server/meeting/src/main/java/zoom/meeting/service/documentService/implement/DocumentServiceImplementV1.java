package zoom.meeting.service.documentService.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.service.documentService.DocumentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImplementV1 implements DocumentService {

    private final NoteRepository noteRepository;
    private final TransactionTemplate txTemplate;

    public DocumentServiceImplementV1(NoteRepository noteRepository, PlatformTransactionManager txManager) {
        this.noteRepository = noteRepository;
        this.txTemplate = new TransactionTemplate(txManager);
    }



    @Override
    public List<Note> findAllDocumentsByNickName(String nickName) {
        return txTemplate.execute(status -> {
            try {
                return noteRepository.findByNickNameAll(nickName);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public Note saveDocument(String userUUID, String roomUUID, String title, String nickName, String text){
        if(title.equals("") && text.equals("")){
            return null;
        }
        if(title.equals("")){
            title = "제목 없음";
        }
        String finalTitle = title;
        return txTemplate.execute(status -> {
            try {
                Optional<Note> findNote = noteRepository.findByUserUUID(userUUID);
                if (findNote.isEmpty()) {
                    return noteRepository.save(new Note(
                            userUUID,
                            roomUUID,
                            getTime(),
                            finalTitle,
                            nickName,
                            text));
                }else{
                    Note note = findNote.get();
                    note.setDate(getTime());
                    note.setTitle(finalTitle);
                    note.setText(text);
                    return findNote.get();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        return now.format(formatter);
    }
}
