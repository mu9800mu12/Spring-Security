package kopo.poly.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;


//Enum이라는
//자바 객체를 사용해보자
//값을 정의 할 때 상수라던지
//사용되는 enum이다
//특정 사용자 정의 함수들 변수들
@AllArgsConstructor
@Getter
public enum userRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

}
