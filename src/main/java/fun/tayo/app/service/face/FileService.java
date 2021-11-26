package fun.tayo.app.service.face;

import fun.tayo.app.dto.UploadFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 저장경로 얻기
     */
    String getStoredPath(UploadFile uploadFile);

    /**
     * 파일id로 파일 객체 얻기
     */
    UploadFile findByFileId(int fileId);

    /**
     * 파일 타입 이미지인지 확인
     */
    boolean isTypeImage(MultipartFile file);
}
