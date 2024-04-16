package entity;

public class Manager {
    public Manager() {
    }

    public Manager(String managerPw) {
        this.managerPw = managerPw;
    }

    private String managerPw;

    public String getManagerPw() {
        return managerPw;
    }

    public void setManagerPw(String managerPw) {
        this.managerPw = managerPw;
    }
}
