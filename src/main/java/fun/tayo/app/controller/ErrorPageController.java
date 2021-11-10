package fun.tayo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error-page")
public class ErrorPageController {

    @RequestMapping("/400")
    public String error400(){
        return "user/error/not-found";
    }

    @RequestMapping("/500")
    public String error500(){
        return "user/error/internal-server";
    }
}
