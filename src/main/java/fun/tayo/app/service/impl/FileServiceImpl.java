package fun.tayo.app.service.impl;

import fun.tayo.app.dao.FileDao;
import fun.tayo.app.dto.UploadFile;
import fun.tayo.app.service.face.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;

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
}
