package zoom.meeting.service.documentService;

import zoom.meeting.domain.note.Note;

import java.util.List;

public interface DocumentService {
    List<Note> findAllDocumentsByNickName(String nickName);
}
