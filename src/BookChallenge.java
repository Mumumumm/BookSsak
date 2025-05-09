import DBConnect.DBBookChallenge;
import DBConnect.User;
import DBConnect.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


public class BookChallenge {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    public BookChallenge() {
    }

    public void bookChallengeMenu(User currentUser) {
        while (true) {
            this.displayCurrentBook(currentUser.getUserId());
            switch (this.menu()) {
                case SELECT1:
                    this.startReadBook(currentUser.getUserId());
                    break;
                case SELECT2:
                    this.inputReadBook(currentUser.getUserId());
                    break;
                case SELECT3:
                    this.changeReadBook(currentUser.getUserId());
                    break;
                case SELECT4:
                    System.out.println("메인 메뉴로 이동\n");
                    return;
            }
        }
    }

    /// ///////////////////////메서드///////////////////////////////////////////

    public void displayCurrentBook(String userid) {
        DBBookChallenge db = new DBBookChallenge();
        db.initDBConnect();
        db.currentReadBook(userid);
        db.releaseDB();
    }

    public int menu() {
        Scanner input = new Scanner(System.in);
        System.out.println("[\uD83D\uDDD3\uFE0F독서 챌린지]");
        System.out.println("1. 독서 시작");
        System.out.println("2. 읽을 책 등록하기");
        System.out.println("3. 책 바꾸기");
        System.out.println("4. 메뉴로 가기");
        System.out.println();
        System.out.print("메뉴선택 : ");

        int select = input.nextInt();
        System.out.println();
        return select;
    }


    public void startReadBook(String userid) {
        DBBookChallenge db = new DBBookChallenge();
        db.initDBConnect();
        if(!db.checkCurrent(userid)){
            System.out.println("🚫현재 읽을 책이 등록되어있지 않습니다.");
            return;
        }
        Timer timer = new Timer();
        String time = timer.bookTimer();
        Scanner input = new Scanner(System.in);
        int pages = 0;
        int flag = 0;
        while (true){
            System.out.println("\uD83D\uDCDD이번 챌린지는 몇 쪽까지 읽으셨나요?");
            pages = input.nextInt();
            input.nextLine();
            flag = db.checkPages(userid, pages);
            if(flag > 0){
                break;
            }
            input.nextLine();
        }
        db.updateReadRecord(userid, time, pages);
        System.out.println();
        System.out.println("*****************************");
        System.out.println("챌린지가 종료되었습니다! 짝짝짝\uD83D\uDC4F \n");
        System.out.println("이번 읽은 페이지 " + pages);
        System.out.println("이번 독서 시간 " + time);
        if(flag == 1){
            System.out.println("\n\uD83D\uDC4F책을 완독했습니다!\uD83D\uDC4F");
        }
        System.out.println("*****************************");
        System.out.println();
        db.releaseDB();
    }

    public void inputReadBook(String userid) {
        DBBookChallenge db = new DBBookChallenge();
        db.initDBConnect();
        if(db.checkCurrent(userid)){
            System.out.println("🚫이미 읽고있는 책이 있습니다. 책 바꾸기를 선택해주세요!");
            System.out.println();
            return;
        }
        HashMap<String, Book> wishList = db.selectWishList(userid);
        if (wishList.isEmpty()) {
            System.out.println("🚫현재 서재에 책이 없습니다. 라이브러리에서 내 서재애 책을 추가해주세요.");
            System.out.println("============================================================");
            db.releaseDB();
            return;
        }
        System.out.println("[📌읽을 책을 골라주세요!] \n");
        Iterator<Map.Entry<String, Book>> iterator = wishList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Book> entry = iterator.next();
            System.out.print(db.reandomEmoji() + " " + entry.getValue().getBookid());
            System.out.print(" / " + entry.getValue().getTitle());
            System.out.print(" / " + entry.getValue().getAuthor());
            System.out.print(" / " + entry.getValue().getPublisher());
            System.out.print(" / " + entry.getValue().getCategory());
            System.out.print(" / 총 " + entry.getValue().getPages() + "쪽\n");
            System.out.println(entry.getValue().getIntroduce());
            System.out.println("==============================================");
        }
        System.out.println();
        try {
            System.out.println("읽을 책의 ISBN을 입력해주세요.");
            Scanner s = new Scanner(System.in);
            String inputIsbn = s.nextLine();
            if (!wishList.containsKey(inputIsbn)) {
                System.out.println("❌잘못 입력했습니다.");
                System.out.println();
                return;
            }
            if (db.inputReadBook(userid, inputIsbn)) {
                System.out.println("\n✅" + wishList.get(inputIsbn).getTitle() + "(으)로 현재 읽는 책을 변경했습니다.");
            }
            System.out.println();
            db.releaseDB();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void changeReadBook(String userid) {
        DBBookChallenge db = new DBBookChallenge();
        db.initDBConnect();
        if(!db.checkCurrent(userid)){
            System.out.println("🚫현재 읽을 책이 등록되어있지 않습니다.\n");
            return;
        }
        HashMap<String, Book> wishList = db.selectWishList(userid);
        if (wishList.isEmpty()) {
            System.out.println("🚫변경할 책이 없습니다. 라이브러리에서 내 서재애 책을 추가해주세요.\n");
            db.releaseDB();
            return;
        }
        System.out.println("[📌변경할 책을 골라주세요!] \n");
        Iterator<Map.Entry<String, Book>> iterator = wishList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Book> entry = iterator.next();
            System.out.print(db.reandomEmoji() + " " + entry.getValue().getBookid());
            System.out.print(" / " + entry.getValue().getTitle());
            System.out.print(" / " + entry.getValue().getAuthor());
            System.out.print(" / " + entry.getValue().getPublisher());
            System.out.print(" / " + entry.getValue().getCategory());
            System.out.print(" / 총 " + entry.getValue().getPages() + "쪽\n");
            System.out.println(entry.getValue().getIntroduce());
            System.out.println("==============================================");
        }
        System.out.println();
        System.out.print("변경할 책의 ISBN을 입력해주세요. ");
        Scanner s = new Scanner(System.in);
        String inputIsbn = s.nextLine();
        System.out.println();
        if(!db.checkWishList(userid, inputIsbn)){

        }
        if (db.changeReadBook(userid, inputIsbn)) {
            System.out.println("\uD83D\uDCDA" + wishList.get(inputIsbn).getTitle() + "(으)로 현재 책을 변경하였습니다.");
        }
        System.out.println();
    }
}
