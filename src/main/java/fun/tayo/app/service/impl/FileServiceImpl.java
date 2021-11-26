package fun.tayo.app.service.impl;

import fun.tayo.app.dao.FileDao;
import fun.tayo.app.dto.UploadFile;
import fun.tayo.app.service.face.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;

    //이미지 확장자
    private final List<String> imageContentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String getStoredPath(UploadFile uploadFile) {
        return uploadPath + uploadFile.getSavedName();
    }

    @Override
    public UploadFile findByFileId(int fileId) {
        return fileDao.selectByFileId(fileId);
    }

    @Override
    public boolean isTypeImage(MultipartFile file) {
        return imageContentTypes.contains(file.getContentType());
    }
}
