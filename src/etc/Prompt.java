package etc;

public enum Prompt {

    // 홈
    MAIN("\n==========================================\n" +
            "============== Cinema Kiosk ==============\n" +
            "=========================================="),
    NEW_MENU_START("\n=========================================="),

    // 영화 예매

    // 영화 예매 취소

    // 관리자 프롬프트 - 영화 목록 보기

    // 관리자 프롬프트 - 영화 목록 추가

    // 관리자 프롬프트 - 영화 목록 삭제

    // 오류
    BAD_INPUT("\n올바르지 않은 입력입니다."),
    NOT_LENGTH10("\n10자 이상 입력해주세요."),
    NOT_EXIST_RESERVATION("\n존재하지 않는 예매 번호입니다.");

    private String prompt;

    Prompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
