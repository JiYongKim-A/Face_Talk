package zoom.meeting.domain.repositoryImpl.myBatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {
    private final MemberRepositoryMapper memberRepositoryMapper;
    @Override
    public Member save(Member member) {
        memberRepositoryMapper.save(member);
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepositoryMapper.findByLoginId(loginId);
    }

    @Override
    public List<Member> findAll() {
        return memberRepositoryMapper.findAll();
    }

    @Override
    public Member updateByLoginId(String beforeLoginId, Member updatedMember) {
        memberRepositoryMapper.updateByLoginId(beforeLoginId,updatedMember);
        return findByLoginId(updatedMember.getLoginId()).get();
    }

    @Override
    public void removeByLoginId(String loginId) {
        memberRepositoryMapper.removeByLoginId(loginId);
    }

    @Override
    public Optional<Member> findByManageSeq(Long manageSeq) {
        return memberRepositoryMapper.findByManageSeq(manageSeq);
    }

    @Override
    public List<String> allLoginId() {
        return memberRepositoryMapper.allLoginId();
    }

    @Override
    public List<String> allNickName() {
        return memberRepositoryMapper.allNickName();
    }
}
