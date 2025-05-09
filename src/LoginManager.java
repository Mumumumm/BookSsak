import DBConnect.DBConnect;
import DBConnect.User;

import java.sql.Date;
import java.util.Scanner;

public class LoginManager {

    private MenuManager mMenu;
    public LoginManager() {}

    public User login() {
        Scanner s = new Scanner(System.in);
        System.out.println("[🔑로그인]");
        System.out.print("아이디 : ");
        String inputId = s.nextLine();
        System.out.print("비밀번호 : ");
        String inputPw = s.nextLine();
        System.out.println();
        DBConnect db = new DBConnect();
        db.initDBConnect();
        User loginUser = db.checkLogin(inputId, inputPw);
        db.releaseDB();
        return loginUser;
    }

    public void signUp(){
        System.out.println("[📋회원가입]");
        System.out.println();
        Scanner s = new Scanner(System.in);
        while (true){
            System.out.print("사용할 아이디 : ");
            String inputId = s.nextLine();
            DBConnect db = new DBConnect();
            db.initDBConnect();
            if(!db.checkUserId(inputId)){
                System.out.println("이미 사용중인 아이디입니다.");
                System.out.println("계속하려면 아무키나 눌러주세요. (나가시려면 Exit)");
                String exit = s.nextLine();
                if(exit.trim().toUpperCase().equals("EXIT")){
                    return;
                }
                continue;
            }
            System.out.print("사용할 비밀번호 : ");
            String inputPw = s.nextLine();
            System.out.print("이름을 입력하세요 : ");
            String inputName = s.nextLine();
            System.out.print("생년월일 6자리를 입력하세요 : ");
            String inputBirth = s.nextLine();
            System.out.print("주소를 입력하세요 : ");
            String inputAddr = s.nextLine();
            System.out.print("회원가입 하시겠습니까? Y | N : ");
            String yn = s.nextLine();
            if(yn.trim().toUpperCase().equals("Y")){
                db.signUp(inputId, inputPw, inputName, inputBirth, inputAddr);
                System.out.println();
                System.out.println(inputName+"님의 회원가입이 완료되었습니다.👏🏻");
                System.out.println();
            }else {
                System.out.println();
                System.out.println("메뉴로 돌아갑니다.");
                System.out.println();
                return;
            }
            db.releaseDB();
            return;
        }
    }
}
