package zoom.meeting.repository.springBootTest.memberRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save Test")
    void saveTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));

        //when
        List<String> allLoginId = memberRepository.allLoginId();
        List<String> allNickName = memberRepository.allNickName();

        //then (loginId)
        assertThat(allLoginId).contains(t1.getLoginId());

        //then (nickName)
        assertThat(allNickName).contains(t1.getNickName());
        Optional<Member> findMem = memberRepository.findByLoginId(t1.getLoginId());
        assertThat(t1).isEqualTo(findMem.get());
        log.info("memberManageSeq = {}", t1.getManageSeq());

    }


    @Test
    @DisplayName("findByLoginId Test")
    void findByLoginIdTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));

        //when
        Optional<Member> findMem = memberRepository.findByLoginId("T1");

        //then
        assertThat(findMem.get()).isEqualTo(t1);

    }

    @Test
    @DisplayName("findByManageSeq Test")
    void findByManageSeq() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));

        //when
        Optional<Member> findMem = memberRepository.findByManageSeq(t1.getManageSeq());

        //then
        assertThat(findMem.get()).isEqualTo(t1);
    }


    @Test
    @DisplayName("updateByLoginId Test")
    void updateByLoginIdTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        //when
        memberRepository.updateByLoginId("T1", new Member(
                "T2", "T2", "T2", "T2"));
        Member t2 = memberRepository.findByLoginId("T2").get();
        //then
        assertThat(t1.getManageSeq()).isEqualTo(t2.getManageSeq());
        assertThat(t1.getLoginId()).isNotEqualTo(t2.getLoginId());

        memberRepository.removeByLoginId("T2");
    }

    @Test
    @DisplayName("removeByLoginId Test")
    void removeByLoginIdTest() {
        //given
        Member t1 = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        //when
        memberRepository.removeByLoginId("T1");
        Optional<Member> byManageSeq = memberRepository.findByManageSeq(t1.getManageSeq());

        //then
        assertThat(byManageSeq).isEmpty();
    }

    @Test
    @DisplayName("allNickName Test")
    void allNickNameTest() {
        //when
        List<String> list1 = memberRepository.allNickName();

        //then
        assertThat(list1.size()).isEqualTo(0);

        //given
        memberRepository.save(new Member("T1", "T1", "T1", "T1"));

        //when
        List<String> list2 = memberRepository.allNickName();

        //then
        assertThat(list2.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("allLoginId Test")
    void allLoginIdTest() {
        //when
        List<String> list1 = memberRepository.allLoginId();

        //then
        assertThat(list1.size()).isEqualTo(0);

        //given
        memberRepository.save(new Member("T1", "T1", "T1", "T1"));

        //when
        List<String> list2 = memberRepository.allLoginId();

        //then
        assertThat(list2.size()).isEqualTo(1);

    }
}
