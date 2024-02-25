package store.tteolione.tteolione.global.config.kafka;

// ApiError.java
public class ApiError {

    private String errorMessage;

    public ApiError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

