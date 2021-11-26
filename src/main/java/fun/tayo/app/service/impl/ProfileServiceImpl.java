package fun.tayo.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import fun.tayo.app.dao.FileDao;
import fun.tayo.app.dao.MemberDao;
import fun.tayo.app.dao.ProfileDao;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.UploadFile;
import fun.tayo.app.service.face.FileService;
import fun.tayo.app.service.face.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;
    private final FileDao fileDao;
    private final MemberDao memberDao;
    private final FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public Member info(int memberId) {
        log.debug("info() memberId : {}", memberId);

        return profileDao.selectMemberById(memberId);
    }

    @Override
    @Transactional
    public ResponseData update(MemberSession memberSession, Member member, String target, String value) {

        final ResponseData validation = isValidation(target, value);
        if (!validation.getResult()) {
            return validation;
        }

        //유효성검사 통과
        switch (target) {
            case "name":
                member.setName(value);
                break;
            case "phone":
                member.setPhone(value);
                break;
            case "password":
                member.setPassword(value);
                break;
        }

        profileDao.update(member);
        memberSession.setProfile(member.getProfile());
        memberSession.setName(member.getName());
        return validation;
    }
    
    @Override
    public ResponseData isValidation(String target, String value) {
        if (!StringUtils.hasText(value)) {
            return new ResponseData(false, "값을 입력해주세요!");
        }

        String pattern = "";
        String message = "입력값이 바르지 않습니다";
        boolean regex;

        //유효성검사 내용 채우기
        switch (target) {
            case "name":
                pattern = "^[가-힣a-zA-Z0-9]{2,10}$";
                message = "이름은 영문자/한글/숫자를 2~10글자 내로 입력하세요";
                break;
            case "phone":
                pattern = "^01[0-9]-?([0-9]{3,4})-?([0-9]{4})$";
                message = "번호가 올바르지 않습니다";
                break;
            case "password":
                pattern = "^[a-zA-Z0-9!@#$%^&*()?_~]{8,20}$";
                message = "비밀번호는 영문자/숫자/특수문자를 8~20글자 내로 입력하세요";
                break;
            case "email":
                pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
                message = "이메일주소가 올바르지 않습니다";
                break;
                
        }

        regex = Pattern.matches(pattern, value);
        return new ResponseData(regex, message);

    }

    @Override
    public boolean isSocial(int memberId) {
        int cnt = profileDao.selectCntHasPassword(memberId);
        return cnt == 0;
    }

    @Override
    public boolean checkPassword(int memberId, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("password", password);
        int result = profileDao.selectCntByIdAndPassword(params);

        return result > 0;
    }

    @Override
    @Transactional
    public ResponseData fileUpload(MultipartFile upFile, MemberSession member) throws IOException {

        boolean TypeImg = fileService.isTypeImage(upFile);

        if (!TypeImg) {
            return new ResponseData(false, "이미지형식이 아닙니다.");
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setMemberId(member.getId());
        uploadFile.setOriginName(upFile.getOriginalFilename());
        uploadFile.setSavedName(UUID.randomUUID().toString());

        Integer fileId = member.getProfile();
        String oldFilePath = null;
        String newFilePath = uploadPath + uploadFile.getSavedName();

        //기존파일이 있는경우
        if (fileId != null) {
            final UploadFile oldFile = fileDao.selectByFileId(fileId);
            oldFilePath = uploadPath + oldFile.getSavedName();
            fileDao.delete(fileId);
        }

        fileDao.insert(uploadFile);
        profileDao.updateProfile(uploadFile);
        upFile.transferTo(new File(newFilePath));

        //파일삭제
        if (oldFilePath != null) {
            final File file = new File(oldFilePath);

            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        member.setProfile(uploadFile.getId());
        return new ResponseData(true, "ok");
    }

}
