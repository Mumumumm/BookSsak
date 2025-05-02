import DBConnect.User;

import java.util.Scanner;

public class BookChallenge {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private User currentUser = null;

    public BookChallenge() {
    }

    public void BookChallengeMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("===================");
        System.out.println("[독서 챌린지]");
        System.out.println("===================");
        System.out.println("여기에 도서목록들");
        System.out.println("1. 독서 시작");
        System.out.println("2. 도서 등록");
        System.out.println("3. 도서 삭제");
        System.out.println("4. 메뉴로 가기");

        int select = input.nextInt();
        System.out.println();// ❗줄띄우기용

        switch (select) {
            case SELECT1:

                break;
            case SELECT2:

                break;
            case SELECT3:

                break;
            case SELECT4:
                return;
        }
    }

    public void bookTimer(){

    }

    public void bookAdd(){

    }

    public void bookDelete(){

    }
}
