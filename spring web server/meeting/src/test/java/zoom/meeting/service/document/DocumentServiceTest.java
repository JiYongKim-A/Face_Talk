package zoom.meeting.service.document;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.service.documentService.DocumentService;


import java.util.List;

@Slf4j
@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private DocumentService documentService;

    private long noteManageSeq;

    @Test
    @DisplayName("documentServiceV1 Test")
    public void documentServiceTest() {
        //given
        Note document = documentService.saveDocument("test1", "test1", "test1", "test1", "test1");
        noteManageSeq = document.getManageSeq();
        //when
        List<Note> list = documentService.findAllDocumentsByNickName("test1");
        //then
        Assertions.assertThat(list).contains(document);

    }

    @AfterEach
    public void afterEach() {
        noteRepository.removeByManageSeq(noteManageSeq);
    }
}
