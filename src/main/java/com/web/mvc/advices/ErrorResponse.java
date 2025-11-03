package com.web.mvc.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends ApiResponse<Object> {
    public ErrorResponse(ApiError apiError) {
        super(apiError);
    }
}
