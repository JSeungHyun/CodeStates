package com.codestates.sample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public String hello() {
    long pid = ProcessHandle.current().pid();
    return String.format("Hello World, Hello BE Bootcamp! @PID : %d", pid);
  }
}
