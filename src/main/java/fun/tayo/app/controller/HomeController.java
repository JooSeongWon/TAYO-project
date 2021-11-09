package fun.tayo.app.controller;

import fun.tayo.app.dao.TestDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final TestDao testDao;

    @Value("${upload.path}")
    private String path;
    @Value("${db.username}")
    private String dbUserName;

    @GetMapping("/")
    public String main(Model model) {

        Map<String, Object> map = testDao.selectOneMember(2);
        log.debug("map {}", map);

        model.addAttribute("path", path);
        model.addAttribute("dbUserName", dbUserName);
        model.addAttribute("loginMember", map);
        model.addAttribute("question", true);

        return "user/sample/user-template";
    }

    @GetMapping("/admin-test")
    public String adminMain() {

        return "admin/sample/admin-template";
    }
}
