package zoom.meeting.domain.repositoryImpl.myBatis.note;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.note.Note;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisNoteRepository implements NoteRepository {

    private final NoteRepositoryMapper noteRepositoryMapper;

    @Override
    public Note save(@Param("note")Note note) {
        noteRepositoryMapper.save(note);
        return note;
    }

    @Override
    public List<Note> findAll() {
        return noteRepositoryMapper.findAll();
    }

    @Override
    public List<Note> findByNickNameAll(String nickName) {
        return noteRepositoryMapper.findByNickNameAll(nickName);
    }

    @Override
    public Optional<Note> findByUserUUID(String userUUID) {
        return noteRepositoryMapper.findByUserUUID(userUUID);
    }

    @Override
    public Optional<Note> findByManageSeq(Long manageSeq) {
        return noteRepositoryMapper.findByManageSeq(manageSeq);
    }

    @Override
    public Note updateByManageSeq(Long manageSeq, Note updatedNote) {
        noteRepositoryMapper.updateByManageSeq(manageSeq, updatedNote);
        return findByManageSeq(manageSeq).get();
    }

    @Override
    public void removeByManageSeq(Long manageSeq) {
        noteRepositoryMapper.removeByManageSeq(manageSeq);
    }
}
