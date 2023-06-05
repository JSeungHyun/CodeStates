package com.example.BareMinimumRequirement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionController {
    @GetMapping("/")
    public String Hello(){
        return "To-do Application !!";
    }
}
