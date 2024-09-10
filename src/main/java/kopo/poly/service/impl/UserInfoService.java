package kopo.poly.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import kopo.poly.auth.AuthInfo;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        log.info("서비스 pDTO : " + pDTO);

        AtomicReference<UserInfoDTO> atomicReference = new AtomicReference<>(); // 람다로 인해 값을 공유하지 못하여 AtomicReference 사용함

        // ifPresentOrElse 값이 존재할 떄와 값이 존재 안할 때, 수행할 내용을 정의(람다 표현식 사용)
        userInfoRepository.findByUserId(pDTO.userId()).ifPresentOrElse(entity -> {
            atomicReference.set(UserInfoDTO.builder().existsYn("Y").build()); // 객체에 값이 존재한다면...

        }, () -> {
            atomicReference.set(UserInfoDTO.builder().existsYn("N").build()); // 값이 존재하지 않는다면...

        });

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        log.info(atomicReference.get().existsYn());

        return atomicReference.get();
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) {

        log.info("[ 유저 서비스 ] : insertUserInfo 시작!");

        int res = 0;

        log.info("pDTO : " + pDTO);

        // 회원 가입 중복 방지를 위해 DB에서 데이터 조회
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(pDTO.userId());

        // 이미 가입된 아이디라면
        if (rEntity.isPresent()) {
            res = 2;

        } else {
            // 회원가입을 위한 Entity 생성
            UserInfoEntity pEntity = UserInfoDTO.of(pDTO);

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            res = 1;
        }

        log.info("[ 유저 서비스 ] : insertUserInfo 끝!");


        return res;
    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info("[유저 서비스 ] : getUserInfo 시작!");

       // 회원 아이디
        String user_id = CmmUtil.nvl(pDTO.userId());

        log.info("user_id :" + user_id);

        UserInfoDTO rDTO = UserInfoDTO.from(userInfoRepository.findByUserId(user_id).orElseThrow());

        log.info("[유저 서비스 ] : getUserInfo 끝!");


        return rDTO;
    }

    /**
     * 스프링 시큐리티에서 로그인 처리를 위해 실행하는 함수
     * 인증 기능 사용시 무조건 만들어야함
     * 컨트롤러가 호출하지 않고 시큐리티가 바로 호출
     * 아이디로 검색하고 시큐리티가 비밀번호가 같은지 판단
     * 틀리면 알아서 UsernameNotFoundException 발생
     */

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("[유저인포 서비스 ] : loadUserByUsername 시작!");

        log.info("userId : " + userId);

        // 로그인 요청한 사용자  아이디 검색
        // SELECT * FROM USER_INFO WHERE USER_ID = 'HGLEE67'
        UserInfoEntity rEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + "Not Found User"));

        // rEntity 데이터를 DTO로 변환하기
        UserInfoDTO rDTO = UserInfoDTO.from(rEntity);
//        UserInfoDTO rDTO = new ObjectMapper().convertValue(rEntity, UserInfoDTO.class);

        // 비밀번호가 맞는지 체크 및 권한 부여를 위해 rDTO를 UserDetails를 구현한 AuthInfo에 넣어주기
        return new AuthInfo(rDTO);
    }


}
