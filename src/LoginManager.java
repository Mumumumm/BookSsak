import DBConnect.User;

import java.sql.Date;
import java.util.Scanner;

public class LoginManager {

    private MenuManager mMenu;

    public LoginManager(MenuManager mMenu) {}

    public User Login() {
        Scanner input = new Scanner(System.in);

            System.out.println("[로그인]");
            System.out.print("1. 아이디를 입력해주세요 ");
            String id = input.nextLine();

            System.out.print("2. 비밀번호를 입력해주세요 ");
            String pw = input.nextLine();

            if (id.equals("123") && pw.equals("123")) {
                System.out.println(); //❗줄띄우기용
                return new User(id,pw,new Date(123));
            } else {
                return null;
            }
    }

}
