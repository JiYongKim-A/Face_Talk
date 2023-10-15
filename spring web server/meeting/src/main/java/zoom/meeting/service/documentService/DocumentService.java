package zoom.meeting.service.documentService;

import zoom.meeting.domain.note.Note;

import java.util.List;

public interface DocumentService {
    Note saveDocument(String userUUID, String roomUUID, String title, String nickName, String Text);
    List<Note> findAllDocumentsByNickName(String nickName);
}
