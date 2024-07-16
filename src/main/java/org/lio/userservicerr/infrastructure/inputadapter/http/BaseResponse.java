package org.lio.userservicerr.infrastructure.inputadapter.http;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseResponse {
    private Object data;
    private String message;
}