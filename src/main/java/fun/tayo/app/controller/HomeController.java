package fun.tayo.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String main() {
        return "home";
    }

    @GetMapping("/test1")
    public String consent() {
        return "user/member/consent";
    }

    @GetMapping("/test2")
    public String emailConfirm() {
        return "user/member/emailConfirm";
    }
}
