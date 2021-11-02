package zoom.meeting.domain.repositoryInterface;

import zoom.meeting.domain.note.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {

    //정리 노트 저장 (C)
    Note save(Note note);

    // 모든 노트 조회 (R)
    List<Note> findAll();

     // 닉네임으로 모든 노트 조회 (R) (O)
    List<Note> findByNickNameAll(String nickName);

    Optional<Note> findByUserUUID(String userUUID);

    // 관리번호로 노트 조회 (R)
    Optional<Note> findByManageSeq(Long manageSeq);

    // 관리번호로 노트 수정 (U)
    Note updateByManageSeq(Long manageSeq, Note updatedNote);

    // 관리번호로 노트 삭제 (D)
    void removeByManageSeq(Long manageSeq);




    // test 용도
    default void clearNoteStore(){};

}
