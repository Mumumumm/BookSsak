import java.util.Scanner;

public class LoginManager {

    MenuManager menu = new MenuManager();

    public LoginManager() {
    }

    public void Login() {
        Scanner input = new Scanner(System.in);

        System.out.println("[로그인]");
        System.out.print("1. 아이디를 입력해주세요 ");
        String id = input.nextLine();
        System.out.print("2. 비밀번호를 입력해주세요 ");
        String pw = input.nextLine();

        System.out.println();// ❗줄띄우기용
        menu.MenuSelect();
    }

}
