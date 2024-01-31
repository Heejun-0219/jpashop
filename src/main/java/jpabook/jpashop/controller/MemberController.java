package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String memberList(@Valid MemberForm memberForm, BindingResult result) { // @Valid -> MemberForm 어노테이션을 사용한다.
        // @Valid 다음에 BindingResult 존재하면, 오류가 result에 담겨서 코드가 실행된다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // PRG
    }

    @GetMapping("/members")
    public String members(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute(members);

        return "memebers/memberList";
    }
}
