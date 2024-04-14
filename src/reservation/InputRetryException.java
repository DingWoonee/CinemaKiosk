package reservation;

public class InputRetryException extends RuntimeException {

    // 기본 생성자
    public InputRetryException() {
        super();
    }

    // 예외 메시지를 포함하는 생성자
    public InputRetryException(String message) {
        super(message);
    }
}
