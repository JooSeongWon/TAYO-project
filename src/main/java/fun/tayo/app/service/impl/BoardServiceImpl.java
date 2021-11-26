package fun.tayo.app.service.impl;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.BoardDao;
import fun.tayo.app.dao.FileDao;
import fun.tayo.app.dto.*;
import fun.tayo.app.service.face.BoardService;
import fun.tayo.app.service.face.FileService;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final WorkSpaceService workSpaceService;
    private final BoardDao boardDao;
    private final FileDao fileDao;
    private final FileService fileService;

    //한페이지에 보여질 게시글 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_LIST_COUNT = 15;
    //보여질 페이지 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_PAGE_COUNT = 5;

    //파일 저장경로
    @Value("${upload.path}")
    private String filePath;

    @Override
    public boolean isNotTeamMemberInWorkSpace(int memberId, int workSpaceId) {
        return workSpaceService.getMyWorkSpaceName(workSpaceId, memberId) == null;
    }

    @Override
    public List<Board> getList(PagingBoardAndMember pagingBoardAndMember) {
        final Paging paging = pagingBoardAndMember.getPaging();

        paging.setListCount(PAGING_LIST_COUNT);
        paging.setPageCount(PAGING_PAGE_COUNT);
        paging.setTotalCount(boardDao.selectTotalCount(pagingBoardAndMember));
        paging.makePaging();

        return boardDao.selectsByPagingBoardAndMember(pagingBoardAndMember);
    }

    @Override
    @Transactional
    public Board getDetail(int boardId, boolean noRead, int memberId, int workSpaceId) {
        final Board board = boardDao.selectDetail(boardId);

        if (board == null || board.getWorkSpaceId() != workSpaceId) {
            return null;
        }

        if (noRead) {
            Map<String, Object> params = new HashMap<>();
            params.put("memberId", memberId);
            params.put("boardId", boardId);

            final int readCheck = boardDao.selectCntReadCheck(params);
            if (readCheck == 0) {
                boardDao.insertReadCheck(params);
            }
        }

        return board;
    }

    @Override
    public boolean checkNoRead(int memberId, int workSpaceId, int categoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("workSpaceId", workSpaceId);
        params.put("categoryId", categoryId);

        int result = boardDao.selectCntNewPost(params);
        return result != 0;
    }

    @Override
    @Transactional
    public int putComments(MemberSession member, int workSpaceId, int boardId, String content) {
        if (!StringUtils.hasText(content) || hasNotGrantBoardAccess(boardId, workSpaceId)) {
            return 0;
        }

        Comments comments = new Comments();
        comments.setContent(content);
        comments.setMember(member);
        comments.setBoardId(boardId);

        boardDao.insertComments(comments);
        return comments.getId();
    }

    @Override
    @Transactional
    public boolean deleteComments(int memberId, int workSpaceId, int boardId, int commentsId) {
        if (hasNotGrantBoardAccess(boardId, workSpaceId) || hasNotGrantCommentsEdit(commentsId, memberId)) {
            return false;
        }

        return boardDao.deleteComments(commentsId) != 0;
    }

    @Override
    @Transactional
    public int createNewPost(Map<String, Object> params) throws IOException {
        //입력값 검증
        if (isNotValidationNewPostParams(params, false)) {
            return 0;
        }

        final int memberId = (int) params.get("memberId");
        final MultipartFile file = (MultipartFile) params.get("file");

        //첨부파일 저장 처리
        String fullPath = null;
        if (!file.isEmpty()) {
            final UploadFile uploadFile = getUploadFile(memberId, file);
            fullPath = filePath + uploadFile.getSavedName();

            fileDao.insert(uploadFile);

            params.put("fileId", uploadFile.getId());
        }
        params.remove("file");

        //게시글 작성
        boardDao.insert(params);
        final Object boardId = params.get("id");
        params.put("boardId", boardId);
        params.remove("id");

        //게시글 읽음처리
        boardDao.insertReadCheck(params);

        //파일 연결, 물리적 저장
        if (fullPath != null) {
            boardDao.insertBoardFileLink(params);
            file.transferTo(new File(fullPath));
        }

        //게시글 id 반환
        return (int) boardId;
    }

    @Override
    public boolean checkBoardNotWrittenFromMember(int boardId, int memberId) {
        return hasNotGrantBoardEdit(boardId, memberId);
    }

    @Override
    @Transactional
    public boolean updatePost(Map<String, Object> params) throws IOException {
        //입력값 검증
        if (isNotValidationNewPostParams(params, true)) {
            return false;
        }

        final int boardId = (int) params.get("boardId");
        final int memberId = (int) params.get("memberId");
        final int workSpaceId = (int) params.get("workSpaceId");

        final Board board = getDetail(boardId, false, memberId, workSpaceId);
        if (board == null) return false;

        //파일처리
        String oldFilePath = null;
        String newFilePath = null;
        final MultipartFile file = params.get("file") != null ? (MultipartFile) params.get("file") : null;
        final Boolean deleteFile = params.get("deleteFile") != null ? (Boolean) params.get("deleteFile") : null;

        if (file != null && !file.isEmpty()) {

            //기존파일 있는경우
            if (board.getUploadFileId() != null) {
                final UploadFile oldFile = fileService.findByFileId(board.getUploadFileId());
                oldFilePath = filePath + oldFile.getSavedName(); //물리 저장경로
                fileDao.delete(oldFile.getId()); //파일삭제 (연결삭제 CASCADE)
            }

            UploadFile uploadFile = getUploadFile(memberId, file);
            newFilePath = filePath + uploadFile.getSavedName();

            fileDao.insert(uploadFile); //파일 저장

            params.put("fileId", uploadFile.getId());
        } else if (//기존 파일 삭제
                deleteFile != null //
                && deleteFile
                && board.getUploadFileId() != null) {

            final UploadFile oldFile = fileService.findByFileId(board.getUploadFileId());
            oldFilePath = filePath + oldFile.getSavedName(); //물리 저장경로
            fileDao.delete(oldFile.getId()); //파일삭제 (연결삭제 CASCADE)
        }
        params.remove("file");

        //게시글 수정
        boardDao.update(params);

        //파일 연결, 물리적 저장
        if (newFilePath != null) {
            boardDao.insertBoardFileLink(params);
            file.transferTo(new File(newFilePath));
        }

        //기존파일 물리적 삭제
        if (oldFilePath != null) {
            final File oldFile = new File(oldFilePath);
            //noinspection ResultOfMethodCallIgnored
            oldFile.delete();
        }

        return true;
    }

    private UploadFile getUploadFile(int memberId, MultipartFile file) {
        String originName = file.getOriginalFilename();
        String saveName = UUID.randomUUID().toString();

        UploadFile uploadFile = new UploadFile();
        uploadFile.setMemberId(memberId);
        uploadFile.setOriginName(originName);
        uploadFile.setSavedName(saveName);
        return uploadFile;
    }

    private boolean isNotValidationNewPostParams(Map<String, Object> params, boolean nullable) {
        final String title = params.get("title") != null ? (String) params.get("title") : null;
        final String content = params.get("content") != null ? (String) params.get("content") : null;
        final MultipartFile file = params.get("file") != null ? (MultipartFile) params.get("file") : null;

        boolean result = (nullable && title == null) || StringUtils.hasText(title);
        result = result && ((nullable && content == null) || StringUtils.hasText(content));
        return !result || (file != null && !file.isEmpty() && !fileService.isTypeImage(file));
    }

    private boolean hasNotGrantBoardAccess(int boardId, int workSpaceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("workSpaceId", workSpaceId);
        params.put("boardId", boardId);

        int checkGrant = boardDao.selectCntBoardInWorkSpace(params);
        return checkGrant == 0;
    }

    private boolean hasNotGrantBoardEdit(int boardId, int memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("boardId", boardId);

        int checkGrant = boardDao.selectCntBoardOfMember(params);
        return checkGrant == 0;
    }

    private boolean hasNotGrantCommentsEdit(int commentsId, int memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("commentsId", commentsId);
        params.put("memberId", memberId);

        int checkGrant = boardDao.selectCntCommentsOfMember(params);
        return checkGrant == 0;
    }


}
