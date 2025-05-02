import java.util.Scanner;

import DBConnect.DBConnect;
import DBConnect.User;

public class MenuManager {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private User currentUser = null;

    public void menuSelect() {
        while (true) {
             this.login();
            while (this.currentUser!=null){
                Boolean endFlag = false;
                System.out.println("[메뉴 선택]");
                System.out.println("1. 도서 챌린지");
                System.out.println("2. 독서 발자취");
                System.out.println("3. 라이브러리");
                System.out.println("4. 로그아웃");

                Scanner input = new Scanner(System.in);
                int menuSelect = input.nextInt();
                input.nextLine();
                System.out.println();// ❗줄띄우기용

                switch (menuSelect) {
                    case SELECT1:
                        BookChallenge bookChallenge = new BookChallenge();
                        bookChallenge.bookChallengeMenu(this.currentUser);
                        break;

                    case SELECT2:
                        FootPrints footPrints = new FootPrints();
                        footPrints.fooPrintsMenu();
                        break;

                    case SELECT3:
                        Library live = new Library();
                        live.libraryMenu();

                        break;

                    case SELECT4:
                        System.out.println("로그아웃 하시겠습니까? Y | N");
                        String s = input.nextLine();
                        if(s.toUpperCase().trim().equals("Y")){
                            this.currentUser = null;
                            endFlag = true;
                            System.out.println("로그아웃 되었습니다");
                            break;
                        }
                }
                if(endFlag){
                    break;
                }
            }
        }
    }

    public void login() {
        Scanner s = new Scanner(System.in);
        System.out.println("아이디를 입력하세요.");
        String inputId = s.nextLine();
        System.out.println("비밀번호를 입력하세요.");
        String inputPw = s.nextLine();
        DBConnect db = new DBConnect();
        db.initDBConnect();
        this.currentUser = db.checkLogin(inputId, inputPw);
        db.releaseDB();
    }
}
