package etc;

public enum Prompt {

    // 홈

    // 영화 예매

    // 영화 예매 취소

    // 관리자 프롬프트 - 영화 목록 보기

    // 관리자 프롬프트 - 영화 목록 추가

    // 관리자 프롬프트 - 영화 목록 삭제

    // 오류
    BAD_INPUT("올바르지 않은 입력입니다."),
    NOT_EXIST_RESERVATION("존재하지 않는 예매 번호입니다.");

    private String prompt;

    Prompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
