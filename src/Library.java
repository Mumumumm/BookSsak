import java.util.Scanner;

public class Library {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private MenuManager mMenu;

    public Library(MenuManager menu) {
        this.mMenu = menu;
    }

    public void LibraryMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("===================");
        System.out.println("[라이브러리]");
        System.out.println("===================");
        System.out.println("1. 베스트셀러");
        System.out.println("2. 도서 검색하기");
        System.out.println("3. 기분에 따른 도서 추천 목록");
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
                mMenu.MenuSelect();
                break;
        }
    }
}
