package fun.tayo.app.service.impl;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.BoardDao;
import fun.tayo.app.dao.FileDao;
import fun.tayo.app.dto.*;
import fun.tayo.app.service.face.BoardService;
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

    //한페이지에 보여질 게시글 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_LIST_COUNT = 15;
    //보여질 페이지 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_PAGE_COUNT = 5;

    //업로드가능 확장자
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");

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
        if (!validateNewPostParams(params)) {
            return 0;
        }

        final int memberId = (int) params.get("memberId");
        final MultipartFile file = (MultipartFile) params.get("file");

        //첨부파일 저장 처리
        if (!file.isEmpty()) {
            String originName = file.getOriginalFilename();
            String saveName = UUID.randomUUID().toString();
            String fullPath = filePath + saveName;

            UploadFile uploadFile = new UploadFile();
            uploadFile.setMemberId(memberId);
            uploadFile.setOriginName(originName);
            uploadFile.setSavedName(saveName);

            fileDao.insert(uploadFile);
            file.transferTo(new File(fullPath));

            params.put("fileId", uploadFile.getId());
        }
        params.remove("file");

        //게시글 작성
        boardDao.insert(params);
        final Object boardId = params.get("id");
        params.put("boardId", boardId);
        params.remove("id");

        //파일 연결
        if (params.containsKey("fileId")) {
            boardDao.insertBoardFileLink(params);
        }

        //게시글 읽음처리
        boardDao.insertReadCheck(params);

        //게시글 id 반환
        return (int) boardId;
    }

    private boolean validateNewPostParams(Map<String, Object> params) {
        final String title = (String) params.get("title");
        final String content = (String) params.get("content");
        final MultipartFile file = (MultipartFile) params.get("file");

        boolean result = StringUtils.hasText(title) && StringUtils.hasText(content);
        return result && (file.isEmpty() || contentTypes.contains(file.getContentType()));
    }

    private boolean hasNotGrantBoardAccess(int boardId, int workSpaceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("workSpaceId", workSpaceId);
        params.put("boardId", boardId);

        int checkGrant = boardDao.selectCntBoardInWorkSpace(params);
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
