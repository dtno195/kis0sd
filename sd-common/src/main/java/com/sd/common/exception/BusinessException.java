package com.sd.common.exception;

import lombok.Getter;

/**
 * The class Business exception.
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * The Code.
     */
    final int code;

    /**
     * Instantiates a new Business exception.
     *
     * @param message the message
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.CODE_BUSINESS;
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param message the message
     * @param code    the code
     */
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
}
