package zoom.meeting.domain.repositoryImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static long manageSequence = 0L;

    private static List<String> idStore = new ArrayList<>();
    private static List<String> nickNameStore = new ArrayList<>();

    @Override
    public List<String> allLoginId() {
        return idStore;
    }

    @Override
    public List<String> allNickName() {
        return nickNameStore;
    }

    @Override
    public Member save(Member member) {
        member.setManageSeq(manageSequence++);
        store.put(member.getManageSeq(), member);

        //중복확인용
        idStore.add(member.getLoginId());
        nickNameStore.add(member.getNickName());
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Member updateByLoginId(String loginId, Member updatedMember) {
        Optional<Member> findByLoginId = findByLoginId(loginId);
        if(findByLoginId.isEmpty()){
            log.error("updateByLoginId Member Not Found error : loginId = {} ",loginId);
            return null;
        }
        Member member = findByLoginId.get();
        member.setName(updatedMember.getName());
        member.setNickName(updatedMember.getNickName());
        member.setPassword(updatedMember.getPassword());
        return member;
    }

    @Override
    public void removeByLoginId(String loginId) {
        Optional<Member> findByLoginId = findByLoginId(loginId);
        if(findByLoginId.isEmpty()){
            log.error("RemoveByLoginId Not Found error : loginId = {} ",loginId);
        }
        store.remove(findByLoginId.get().getManageSeq());
    }

    @Override
    public void clearMemStore() {
        store.clear();
    }
}
