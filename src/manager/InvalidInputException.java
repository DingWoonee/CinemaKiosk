package manager;

public class InvalidInputException extends RuntimeException{

    // 기본 생성자
    public InvalidInputException() {
        super();
    }

    // 예외 메시지를 포함하는 생성자
    public InvalidInputException(String message) {
        super(message);
    }
}
