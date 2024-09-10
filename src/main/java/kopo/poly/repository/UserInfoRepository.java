package kopo.poly.repository;

import java.util.Optional;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {

    // 회원 정보 조회
    Optional<UserInfoEntity>findByUserId(String userId);

    // 로그인
    Optional<UserInfoEntity> findByUserIdAndPassword(String userId, String password);

}
