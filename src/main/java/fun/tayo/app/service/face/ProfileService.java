package fun.tayo.app.service.face;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.dto.UploadFile;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

	Member info(int memberId);

	ResponseData update(MemberSession memberSession, Member member, String target, String value);
	
	boolean isSocial(int memberId);

	boolean checkPassword(int memberId, String password);

	ResponseData fileUpload(MultipartFile upFile, MemberSession member) throws IOException;

	public ResponseData isValidation(String target, String value);
}
