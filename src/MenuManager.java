import java.util.Scanner;

public class MenuManager {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    LoginManager login;
    BookChallenge bc;
    FootPrints fp;
    Library live;
    User mUser;

    public MenuManager() {
        bc = new BookChallenge(this); // this = 현재 MenuManager를 전달
        fp = new FootPrints(this);
        live = new Library(this);
        login = new LoginManager(this);
    }

    public void MenuSelect() {
        Scanner input = new Scanner(System.in);

        while (mUser == null || !mUser.isLogin()) {
            mUser = login.Login();
            System.out.println(mUser.getUserId() + " 환영합니다");
            if (mUser == null) {
                System.out.println("아이디 또는 비밀번호 오류");
            }
        }

        System.out.println("[메뉴 선택]");
        System.out.println("1. 도서 챌린지");
        System.out.println("2. 독서 발자취");
        System.out.println("3. 라이브러리");
        System.out.println("4. 사용종료");

        int menuSelect = input.nextInt();
        System.out.println();// ❗줄띄우기용

        switch (menuSelect) {
            case SELECT1:
                bc.BookChallengeMenu();
                break;

            case SELECT2:
                fp.FooPrintsMenu();
                break;

            case SELECT3:
                live.LibraryMenu();
                break;

            case SELECT4:
                System.out.println("사용을 종료합니다");
                break;
        }
    }

    public void Exit(){
        System.out.println("메뉴로");
    }

}
