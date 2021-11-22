package fun.tayo.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    @Value("${upload.path}")
    private String path;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("path", path);
        return "home";
    }

    @GetMapping("/admin")
    public String adminMain() {

        return "admin/sample/admin-template";
    }
}
