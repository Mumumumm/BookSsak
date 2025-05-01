import java.util.Scanner;

public class FootPrints {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private MenuManager mMenu;

    public FootPrints(MenuManager menu) {
        this.mMenu = menu;
    }

    public void FooPrintsMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("===================");
        System.out.println("[독서 발자취]");
        System.out.println("===================");
        System.out.println("1. 내가 읽은 도서 목록");
        System.out.println("2. 내 독서 통계");
        System.out.println("3. 다독왕 랭킹");
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
