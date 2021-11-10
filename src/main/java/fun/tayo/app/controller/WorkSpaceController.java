package fun.tayo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/work-spaces")
public class WorkSpaceController {

    @GetMapping
    public String workSpaces() {
        return "user/work-space/list";
    }
}
