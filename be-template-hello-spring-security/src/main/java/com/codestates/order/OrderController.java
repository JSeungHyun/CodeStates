package com.codestates.order;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @GetMapping
    public String getOrders(Model model) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "order";
    }
}
