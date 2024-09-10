package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserInfoService extends UserDetailsService {

    // 아이디 중복 쳌
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    // 회원 가입
    int insertUserInfo(UserInfoDTO pDTO);

    // 본인 회원 정보 조회
    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

}
