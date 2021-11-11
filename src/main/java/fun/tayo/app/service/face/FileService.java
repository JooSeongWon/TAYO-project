package fun.tayo.app.service.face;

import fun.tayo.app.dto.UploadFile;

public interface FileService {

    /**
     * 저장경로 얻기
     */
    String getStoredPath(UploadFile uploadFile);

    /**
     * 파일id로 파일 객체 얻기
     */
    UploadFile findByFileId(int fileId);
}
