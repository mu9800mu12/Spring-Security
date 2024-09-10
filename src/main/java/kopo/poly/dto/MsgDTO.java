package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_DEFAULT)
public record MsgDTO(int result // 결과 코드
                   , String msg // 결과 메세지

 ) {

}
