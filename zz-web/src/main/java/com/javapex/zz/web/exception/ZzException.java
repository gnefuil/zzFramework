package com.javapex.zz.web.exception;

import com.javapex.zz.web.enums.StatusEnum;

public class ZzException extends GenericException {

    public ZzException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ZzException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ZzException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ZzException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public ZzException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public ZzException(Exception oriEx) {
        super(oriEx);
    }

    public ZzException(Throwable oriEx) {
        super(oriEx);
    }

    public ZzException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public ZzException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }
}
