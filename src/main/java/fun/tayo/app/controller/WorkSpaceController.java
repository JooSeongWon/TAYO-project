package fun.tayo.app.controller;

import fun.tayo.app.dto.WorkSpace;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/work-spaces")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping
    public String workSpaces(HttpServletRequest request, Model model) {

        final List<WorkSpace> workSpaceList = workSpaceService.getWorkSpaces(request);
        model.addAttribute("workSpaceList", workSpaceList);

        return "user/work-space/list";
    }
}
