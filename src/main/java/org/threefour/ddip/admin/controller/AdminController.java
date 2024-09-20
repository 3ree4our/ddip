package org.threefour.ddip.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;
import org.threefour.ddip.product.repository.ProductRepositoryImpl;

import java.util.List;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController extends BaseEntity {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryImpl;


    @GetMapping("admin")
    public String admin(Model model){

        long Membercount = memberRepository.countBy();
        log.info("Membercount        : " + Membercount);
        model.addAttribute("membercount", Membercount);

        return "admin/admin";
    }
    @GetMapping("memberList")
    public String memberList(Model model){
       List<Member> memberList = memberRepository.findActiveMembers();
       model.addAttribute("memberList", memberList);
       return "admin/memberList";
    }
    @GetMapping("memberDel")
    public String memberDel(Long memberId){
        log.info("memberDel         : " + memberId);
        memberRepository.delete_yn(memberId);
        return "redirect:memberList";
    }
    @GetMapping("memberGroupBy")
    public @ResponseBody  String[] memberGroupBy(){
         String[] mem = memberRepository.findgroupByMember();
        System.out.println("                mem  "+ mem[0]);
        //long Membercount = memberRepository.countBy();
        return mem;
    }
    @GetMapping("keyword")
    public String keyword(){
        return "admin/keyword";
    }

    @PostMapping("search")
    public String search(String type , String search ,Model model){
        System.out.println(type + "     "+ search);
        List<Product> list = productRepositoryImpl.selectBySearch(type,search);
        model.addAttribute("list",list);
        return "admin/keyword";
    }
}
