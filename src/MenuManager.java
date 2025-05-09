import java.util.Scanner;

import DBConnect.DBConnect;
import DBConnect.User;

public class MenuManager {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;
    private final int SELECT5 = 5;

    private User currentUser = null;

    public void menuSelect() {
        while (true) {
            System.out.println();
            System.out.println("*******************************");
            System.out.println("\t\t북싹 읽었수다");
            System.out.println("\t  Welcome to BookSsak");
            System.out.println("*******************************\n");

            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 종료");
            System.out.println();
            System.out.print("메뉴선택 : ");
            Scanner s = new Scanner(System.in);
            int select = s.nextInt();
            s.nextLine();
            System.out.println();
            LoginManager loginManager = new LoginManager();
            switch (select){
                case 1:
                    this.currentUser = loginManager.login();
                    break;
                case 2:
                    loginManager.signUp();
                    break;
                case 3:
                    System.out.println("");
                    System.out.println("종료되었습니다.");
                    return;
            }

            while (this.currentUser!=null){
                Boolean endFlag = false;
                switch (this.menu()) {
                    case SELECT1:
                        BookChallenge bookChallenge = new BookChallenge();
                        bookChallenge.bookChallengeMenu(this.currentUser);
                        break;
                    case SELECT2:
                        FootPrints footPrints = new FootPrints();
                        footPrints.fooPrintsMenu(this.currentUser);
                        break;
                    case SELECT3:
                        Library live = new Library();
                        live.libraryMenu(this.currentUser);
                        break;
                    case SELECT4:
                        Shop shop = new Shop();
                        shop.shopManager(this.currentUser);
                        break;
                    case SELECT5:
                        System.out.println("로그아웃 하시겠습니까? Y | N");
                        Scanner input = new Scanner(System.in);
                        String yn = input.nextLine();
                        if(yn.toUpperCase().trim().equals("Y")){
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

    public int menu (){
        System.out.println("[📋메뉴 선택]");
        System.out.println("1. 독서 챌린지");
        System.out.println("2. 독서 발자취");
        System.out.println("3. 라이브러리");
        System.out.println("4. 포인트샵");
        System.out.println("5. 로그아웃");
        System.out.println();
        System.out.print("메뉴선택 : ");
        Scanner input = new Scanner(System.in);
        int menuSelect = input.nextInt();
        input.nextLine();
        System.out.println();// ❗줄띄우기용
        return menuSelect;
    }
}
