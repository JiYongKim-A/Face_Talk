package zoom.meeting.service.documentService.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.service.documentService.DocumentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImplementV1 implements DocumentService {

    private final NoteRepository noteRepository;
    @Override
    public List<Note> findAllDocumentsByNickName(String nickName) {
        return noteRepository.findByNickNameAll(nickName);
    }
}
