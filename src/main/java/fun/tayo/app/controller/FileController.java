package fun.tayo.app.controller;

import fun.tayo.app.dto.UploadFile;
import fun.tayo.app.service.face.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@Controller
public class FileController {

    private final FileService fileService;

    @ResponseBody
    @GetMapping("/img/{fileId}")
    public ResponseEntity<Resource> showImage(@PathVariable int fileId) throws MalformedURLException {
        final UploadFile file = fileService.findByFileId(fileId);
        final String fullPath = fileService.getStoredPath(file);

        final Resource resource = new UrlResource("file:" + fullPath);
        final String extension = StringUtils.getFilenameExtension(file.getOriginName());

        assert extension != null;
        String contentType = "image/" + extension.toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}