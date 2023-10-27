package zoom.meeting.domain.repositoryImpl.memory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MemoryNoteRepository implements NoteRepository {
    private static Map<Long, Note> store = new ConcurrentHashMap<>();
    private static long manageSequence = 0L;

    @Override
    public Note save(Note note) {
        note.setManageSeq(manageSequence++);
        store.put(note.getManageSeq(), note);
        return note;
    }

    @Override
    public List<Note> findByNickNameAll(String nickName) {

        return findAll().stream()
                .filter(n->n.getNickName().equals(nickName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Note> findByManageSeq(Long manageSeq) {
        return findAll().stream()
                .filter(m->m.getManageSeq().equals(manageSeq))
                .findFirst();
    }

    @Override
    public Optional<Note> findByUserUUID(String userUUID) {
        return findAll().stream()
                .filter(n->n.getUserUUID().equals(userUUID))
                .findFirst();
    }

    @Override
    public List<Note> findAll() {
        return new ArrayList<>(store.values());
    }


    @Override
    public Note updateByManageSeq(Long manageSeq, Note updatedNote) {
        Optional<Note> findByManageSeq = findByManageSeq(manageSeq);
        if(findByManageSeq.isEmpty()){
            log.error("updateByManageSeq Note Not Found error : manageSeq = {} ",manageSeq);
            return null;
        }
        Note note = findByManageSeq.get();
        note.setDate(updatedNote.getDate());
        note.setTitle(updatedNote.getTitle());
        note.setText(updatedNote.getText());
//        note.setContent(updatedNote.getContent()); 이후 변경 예정

        return note;
    }

    @Override
    public void removeByManageSeq(Long manageSeq) {
        Optional<Note> byManageSeq = findByManageSeq(manageSeq);
        if(byManageSeq.isEmpty()){
            log.error("removeByManageSeq Note Not Found error : manageSeq = {} ",manageSeq);
        }
        store.remove(manageSeq);
    }

}
