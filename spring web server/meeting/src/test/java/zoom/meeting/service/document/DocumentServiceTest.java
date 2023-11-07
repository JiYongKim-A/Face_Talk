package zoom.meeting.service.document;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.note.Note;
import zoom.meeting.service.documentService.DocumentService;


import java.util.List;

@Slf4j
@Transactional
@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;


    @Test
    @DisplayName("documentServiceV1 Test")
    public void documentServiceTest() {
        //given
        Note document = documentService.saveDocument("test1", "test1", "test1", "test1", "test1");
        //when
        List<Note> list = documentService.findAllDocumentsByNickName("test1");
        //then
        Assertions.assertThat(list).contains(document);

    }
}
