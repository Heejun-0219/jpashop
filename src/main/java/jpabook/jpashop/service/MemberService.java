package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor // lombok => 필드의 모든 것을 가지고 생성자를 만들어 준다.
@RequiredArgsConstructor // lombok => 파이널 필드를 가지고 생성자를 만들어 준다.
public class MemberService {

    //    @Autowired
    private final MemberRepository memberRepository; // final로 변경해서, 컴파일 시점으로 세팅을 옮겨줄 수 있다.

//    @Autowired // setter injection => 테스트하기 쉬움 but, 운영환경에서 변경이 가능하기 떄문에 문제가 생길 수 있다.
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // 따라서 생성자 주입을 사용한다. 테스트하기 쉽고, 안전하다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } -> All

    /*
        회원가입
     */
    @Transactional

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION check
        List<Member> byName = memberRepository.findByName(member.getName());
        if (!byName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /*
        회원 전체 조회
     */
//    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /*
        회원 조회
     */
    //    @Transactional(readOnly = true) // 성능 증가
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
