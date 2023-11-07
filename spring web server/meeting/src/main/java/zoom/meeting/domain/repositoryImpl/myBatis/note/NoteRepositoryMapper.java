package zoom.meeting.domain.repositoryImpl.myBatis.note;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zoom.meeting.domain.note.Note;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NoteRepositoryMapper {

    //정리 노트 저장 (C)
    void save(Note note);

    // 모든 노트 조회 (R)
    List<Note> findAll();

    // 닉네임으로 모든 노트 조회 (R)
    List<Note> findByNickNameAll(String nickName);

    Optional<Note> findByUserUUID(String userUUID);

    // 관리번호로 노트 조회 (R)
    Optional<Note> findByManageSeq(Long manageSeq);

    // 관리번호로 노트 수정 (U)
    void updateByManageSeq(@Param("manageSeq") Long manageSeq, @Param("updatedNote") Note updatedNote);

    // 관리번호로 노트 삭제 (D)
    void removeByManageSeq(Long manageSeq);

}
