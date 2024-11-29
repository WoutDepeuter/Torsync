package org.ehb.wout.torsync.controller;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/wout")
public class HelloWorldController {

    @GetMapping("/help")
    public String sayHello(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "hello.html";  // Returns the hello.html view (Thymeleaf will render it)
    }
}
