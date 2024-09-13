package org.threefour.ddip.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.member.service.MemberService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String getRegistrationForm() {
        return "member/login";
    }

    @GetMapping("/signUp")
    public String joinForm() {
        return "member/registration";
    }

    @PostMapping("/signUp")
    public String join(MemberRequestDTO memberRequestDTO, Model model) {
        System.out.println("Controller: " + memberRequestDTO.getEmail() + " " + memberRequestDTO.getPassword());
        model.addAttribute("member", memberService.join(memberRequestDTO));
        return "redirect:/member/signIn";
    }
}