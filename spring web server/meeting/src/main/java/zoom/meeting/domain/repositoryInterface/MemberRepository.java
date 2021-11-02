package zoom.meeting.domain.repositoryInterface;

import zoom.meeting.domain.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository {

    //회원 저장 (C)
    Member save(Member member);

    // LoginId로 회원 조회 (R)
    Optional<Member> findByLoginId(String loginId);

    //모든 회원 조회 (R)
    List<Member> findAll();

    // 회원 정보 업데이트 (U)
    Member updateByLoginId(String loginId, Member updatedMember);

    // 회원 탈퇴 (D)
    void removeByLoginId(String loginId);


    // ManageSeq 이용시 사용
    default Optional<Member> findByManageSeq(Long manageSeq){
        return null; }

    // test 용도
    default void clearMemStore(){}


    //중복 확인 용도
    default List<String>allLoginId(){
        return null;
    }

    default List<String>allNickName(){
        return null;
    }

}
