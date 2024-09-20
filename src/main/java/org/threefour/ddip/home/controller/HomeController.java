package org.threefour.ddip.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }

    @GetMapping("/blog")
    public ModelAndView blog() {
        ModelAndView view = new ModelAndView();
        view.setViewName("blog");
        return view;
    }

    @GetMapping("/blog-details")
    public ModelAndView blogDetails() {
        ModelAndView view = new ModelAndView();
        view.setViewName("blog-details");
        return view;
    }

    @GetMapping("/checkout")
    public ModelAndView checkout() {
        ModelAndView view = new ModelAndView();
        view.setViewName("checkout");
        return view;
    }

    @GetMapping("/contact")
    public ModelAndView contact() {
        ModelAndView view = new ModelAndView();
        view.setViewName("contact");
        return view;
    }

    @GetMapping("/product-details/{productId}")
    public ModelAndView productDetails(@PathVariable Long productId) {
        ModelAndView view = new ModelAndView();
        view.setViewName("product-details");
        return view;
    }

    @GetMapping("/shop")
    public ModelAndView shop() {
        ModelAndView view = new ModelAndView();
        view.setViewName("shop");
        return view;
    }

    @GetMapping("/shop-cart")
    public ModelAndView shopCart() {
        ModelAndView view = new ModelAndView();
        view.setViewName("shop-cart");
        return view;
    }
}
