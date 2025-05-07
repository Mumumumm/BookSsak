import DBConnect.DBConnect;
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
                    System.out.println("메뉴로 이동");
                    return;
            }
        }
    }

    /// ///////////////////////메서드///////////////////////////////////////////

    public void displayCurrentBook(String userid) {
        DBConnect db = new DBConnect();
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
        Timer timer = new Timer();
        ResultReadBook result = timer.bookTimer(userid); // 시간, 페이지 result에 리턴
        DBConnect db = new DBConnect();
        db.initDBConnect();
        db.updateReadRecord(userid, result.getReadTime());
        db.updateReadPage(userid, result.getReadPages());
        db.releaseDB();
    }

    public void inputReadBook(String userid) {
        DBConnect db = new DBConnect();
        db.initDBConnect();
        HashMap<String, Book> wishList = db.selectWishList(userid);
        if (wishList.isEmpty()) {
            System.out.println("찜 목록이 비어있습니다. 라이브러리에서 내 서재애 책을 추가해주세요.");
            db.releaseDB();
            return;

        }
        System.out.println("현재 찜목록 : ");
        Iterator<Map.Entry<String, Book>> iterator = wishList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Book> entry = iterator.next();
            System.out.println("ISBN / " + entry.getValue().getBookid());
            System.out.println("책 제목 / " + entry.getValue().getTitle());
            System.out.println("저자 / " + entry.getValue().getAuthor());
            System.out.println("출판사 / " + entry.getValue().getPublisher());
            System.out.println("한줄 소개 / " + entry.getValue().getIntroduce());
            System.out.println("카테고리 / " + entry.getValue().getCategory());
            System.out.println("키워드 / " + entry.getValue().getKeyword());
            System.out.println("총 페이지 수 / " + entry.getValue().getPages() + " 쪽");
            System.out.println("==============================================");
        }
        try {
            System.out.print("읽을 책의 isbn을 입력하세요. ");
            Scanner s = new Scanner(System.in);
            String inputIsbn = s.nextLine();
            if (!wishList.containsKey(inputIsbn)) {
                System.out.println("잘못입력하였습니다.");
                return;
            }
            if (db.inputReadBook(userid, inputIsbn)) {
                System.out.println(wishList.get(inputIsbn).getTitle() + "(으)로 현재 읽는 책을 변경하였습니다.");
            } else {
                System.out.println("이미 등록되어 있는 책입니다.");
            }
            db.releaseDB();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void changeReadBook(String userid) {
        DBConnect db = new DBConnect();
        db.initDBConnect();
        HashMap<String, Book> wishList = db.selectWishList(userid);
        if (wishList.isEmpty()) {
            System.out.println("내 서재가 비어있습니다. 라이브러리에서 내 서재애 책을 추가해주세요.");
            db.releaseDB();
            return;
        }
        System.out.println("내 서재 목록 : ");
        Iterator<Map.Entry<String, Book>> iterator = wishList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Book> entry = iterator.next();

            System.out.println("ISBN / " + entry.getValue().getBookid());
            System.out.println("책 제목 / " + entry.getValue().getTitle());
            System.out.println("저자 / " + entry.getValue().getAuthor());
            System.out.println("출판사 / " + entry.getValue().getPublisher());
            System.out.println("한줄 소개 / " + entry.getValue().getIntroduce());
            System.out.println("카테고리 / " + entry.getValue().getCategory());
            System.out.println("키워드 / " + entry.getValue().getKeyword());
            System.out.println("총 페이지 수 / " + entry.getValue().getPages() + " 쪽");
            System.out.println("==============================================");
        }
        System.out.print("변경할 책의 ISBN : ");
        Scanner s = new Scanner(System.in);
        String inputIsbn = s.nextLine();
        System.out.println();
        if (db.changeReadBook(userid, inputIsbn)) {
            System.out.println("\uD83D\uDCDA" + wishList.get(inputIsbn).getTitle() + "(으)로 현재 책을 변경하였습니다.");
        } else {
            System.out.println("오류");
        }
    }
}
