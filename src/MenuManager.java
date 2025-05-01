import java.util.Scanner;

public class MenuManager {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;

    public MenuManager(){}

    public void MenuSelect(){
        Scanner input = new Scanner(System.in);

        System.out.println("[메뉴 선택]");
        System.out.println("1. 도서 챌린지");
        System.out.println("2. 독서 발자취");
        System.out.println("3. 라이브러리");

        int menuSelect = input.nextInt();

        switch (menuSelect){
            case SELECT1:
                break;

            case SELECT2:
                break;

            case SELECT3:
                break;
        }
    }

}
