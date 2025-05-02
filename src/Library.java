import DBConnect.User;

import java.util.Scanner;

public class Library {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private User currentUser = null;

    public Library() {

    }

    public void LibraryMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("===================");
        System.out.println("[라이브러리]");
        System.out.println("===================");
        System.out.println("1. 베스트셀러");
        System.out.println("2. 도서 검색하기");
        System.out.println("3. 오늘은 뭘 읽을까?");
        System.out.println("4. 찜 목록");
        System.out.println("5. 메뉴로 가기");

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

    public void bestSeller(){

    }

    public void search(){

    }

    public void keyWord(){

    }

    public void wishList(){

    }
}
