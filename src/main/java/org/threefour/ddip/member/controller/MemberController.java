package org.threefour.ddip.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.address.service.AddressService;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AddressService addressService;


    @GetMapping("/registration-form")
    public String getRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("address", new Address());
        return "member/member-address";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Member member, @ModelAttribute Address address, Model model) {


        //멤버 저장
        //member.setSchoolAuthYn(true);
        //memberService.saveMember(member);

        //x, y 좌표 구하기
        Address xyAddress = addressService.getCoordinates(address.getRoadAddress());
        xyAddress.setZipcode(address.getZipcode());
        //System.out.println("우편번호: "+address.getZipcode());
        System.out.println("경도 "+xyAddress.getLat());
        xyAddress.setDetailedAddress(address.getDetailedAddress());
        xyAddress.setMember(member);
        System.out.println(xyAddress);

        //주소 저장
        memberService.saveMember(member, xyAddress);

        model.addAttribute("member", member);
        model.addAttribute("address", xyAddress);
        return "member/result";
    }
}
