package com.web.mvc.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> extends ApiResponse<T> {
    public SuccessResponse(T data) {
        super(data);
    }
}
