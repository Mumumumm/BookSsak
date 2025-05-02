import DBConnect.DBConnect;
import DBConnect.User;
import DBConnect.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Library {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    private User currentUser = null;

    public Library() {

    }

    public void libraryMenu() {
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
                this.search();
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
        System.out.println("검색할 도서 제목을 입력해주세요");
        Scanner input = new Scanner(System.in);
        String bookname = input.nextLine();

        DBConnect db = new DBConnect();
        db.initDBConnect();
        HashMap<String, Book> resultList = db.searchBook(bookname);
        Iterator<Map.Entry<String, Book>> iterator = resultList.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Book> entry = iterator.next();
            System.out.println("========================================================");
            System.out.println("bookNo." + entry.getValue().getBookid() + db.reandomEmoji());
            System.out.println("책 제목 : " + entry.getValue().getTitle());
            System.out.println("책 저자 : " + entry.getValue().getAuthor());
            System.out.println("출판사 : " + entry.getValue().getPublisher());
            System.out.println("한 줄 소개 : " + entry.getValue().getIntroduce());
            System.out.println("카테고리 : " + entry.getValue().getCategory());
            System.out.println("책 키워드 : " + entry.getValue().getKeyword());
            System.out.println("페이지 수 : " + entry.getValue().getPages() + "쪽");
            System.out.println("========================================================");

        }
        db.releaseDB();

    }

    public void keyWord(){

    }

    public void wishList(){

    }
}
