package reservation;

public class GoHomePromptException extends RuntimeException {
    public GoHomePromptException(String message) {
        super(message);
    }
    // 기본 생성자
    public GoHomePromptException() {
        super();
    }
}

