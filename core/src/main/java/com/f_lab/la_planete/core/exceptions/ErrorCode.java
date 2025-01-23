package com.f_lab.la_planete.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum ErrorCode {
  FOOD_NOT_EXIST_EXCEPTION("상품이 존재하지 않습니다.", 400),



  // LOCK
  LOCK_ACQUISITION_FAIL_EXCEPTION("현재 너무 많은 요청을 처리하고 있습니다. 다시 시도해주세요",503),

  // KAFKA
  KAFKA_RETRY_FAIL_EXCEPTION("오류 발생! 잠시 후 다시 시도해주세요.", 503),

  ;

  private String description;
  private int status;
}
