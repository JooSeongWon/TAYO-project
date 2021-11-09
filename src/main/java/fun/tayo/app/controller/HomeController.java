package fun.tayo.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @Value("${upload.path}")
    private String path;
    @Value("${db.username}")
    private String dbUserName;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("path", path);
        model.addAttribute("dbUserName", dbUserName);
        model.addAttribute("loginMember", true);
        model.addAttribute("question", true);

        return "user/sample/user-template";
    }

    @GetMapping("/admin-test")
    public String adminMain() {

        return "admin/sample/admin-template";
    }
}
