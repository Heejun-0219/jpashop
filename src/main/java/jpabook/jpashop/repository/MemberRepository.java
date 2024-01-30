package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor // springboot를 사용해서 가능한 기능, 단일성을 위해 사용한다.
public class MemberRepository {

    //    @PersistenceContext // spring -> 자동으로 주입 받음
    private final EntityManager em;
    
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); // sql로 번역시 테이블에 대한 쿼리가 아닌 멤버에 대한 쿼리로 만드는 것이다.
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name)
                .getResultList();
    }
}
