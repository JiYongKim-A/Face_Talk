package zoom.meeting.domain.repositoryImpl.myBatis.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zoom.meeting.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberRepositoryMapper {

    //회원 저장 (C)
    void save(Member member);

    // LoginId로 회원 조회 (R)
    Optional<Member> findByLoginId(String loginId);

    //모든 회원 조회 (R)
    List<Member> findAll();

    // 회원 정보 업데이트 (U)
    void updateByLoginId(@Param("beforeLoginId") String beforeLoginId, @Param("updatedMember") Member updatedMember);

    // 회원 탈퇴 (D)
    void removeByLoginId(String loginId);


    // ManageSeq 이용시 사용
    Optional<Member> findByManageSeq(Long manageSeq);


    //중복 확인 용도
    List<String> allLoginId();


    List<String> allNickName();
}
