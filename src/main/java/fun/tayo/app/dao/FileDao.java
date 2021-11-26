package fun.tayo.app.dao;

import fun.tayo.app.dto.UploadFile;

public interface FileDao {

    /**
     * file id로 file 조회
     */
    UploadFile selectByFileId(int fileId);

    /**
     * file 저장
     */
    void insert(UploadFile uploadFile);

}
