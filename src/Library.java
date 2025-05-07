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
        private final int SELECT5 = 5;

        public void libraryMenu(User currentUser) {
            Scanner input = new Scanner(System.in);
            System.out.println("[\uD83D\uDD0D라이브러리]");
            System.out.println("1. 인기 도서");
            System.out.println("2. 도서 검색하기");
            System.out.println("3. 오늘은 뭘 읽을까?");
            System.out.println("4. 내가 찜한 리스트");
            System.out.println("5. 메뉴로 가기");

            int select = input.nextInt();
            System.out.println();// ❗줄띄우기용

            switch (select) {
                case SELECT1:
                    this.popularBook(currentUser.getUserId());
                    break;
                case SELECT2:
                    this.searchBook(currentUser.getUserId());
                    break;
                case SELECT3:
                    this.moodBasedBookRecommender(currentUser.getUserId());
                    break;
                case SELECT4:
                    this.wishlist(currentUser.getUserId());
                    break;
                case SELECT5:
                    return;
            }
        }

        // 인기 도서
        public void popularBook(String userid) {
            DBConnect db = new DBConnect();
            db.initDBConnect();

            // 유저들이 읽었던 책 목록 데이터를 기준으로 인기 도서 선정
            System.out.println("[인기 도서]");
            HashMap<String, Book> popular = db.getPopularBooks();

            if (popular.isEmpty()) {
                System.out.println("인기 도서 목록을 준비 중입니다.");
            } else {
                int count = 1;
                Iterator<Map.Entry<String, Book>> iterator = popular.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Book> entry = iterator.next();
                    String key = entry.getKey();
                    Book book = entry.getValue();
                    System.out.println(count + ". " + book.getTitle() + " / " + book.getAuthor() + " / " + book.getPublisher());
                    count++;
                }
            }
            System.out.println();

            db.releaseDB();
        }

        // 도서 검색
        public void searchBook(String userid) {
            DBConnect db = new DBConnect();
            db.initDBConnect();

            System.out.println("[도서 검색]");
            Scanner input = new Scanner(System.in);
            System.out.println("검색할 도서를 입력하세요.");
            String search = input.nextLine();

            System.out.println();
            System.out.print("🔍" + "'"+ search +"'" + " 검색 도서\n");
            HashMap<String, Book> resultBooks = db.searchBook(search);

            if (resultBooks.isEmpty()) {
                System.out.println();
                db.releaseDB();
                return;
            }

            Iterator<Map.Entry<String, Book>> iterator = resultBooks.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Book> entry = iterator.next();
                String key = entry.getKey();
                Book book = entry.getValue();
                System.out.println(db.reandomEmoji() + key +
                        " / " + book.getTitle() +
                        " / " + book.getAuthor() +
                        " / " + book.getPublisher() +
                        " / " + book.getCategory() +
                        " / 총" + book.getPages() + "쪽\n" +
                        book.getIntroduce());
                System.out.println("==========================================");
            }

            System.out.println("찜 목록에 추가 (Y / N)");
            String yn = input.nextLine();
            String addWish = "";

            if (yn.equalsIgnoreCase("Y")) {
                System.out.println("찜 목록에 추가할 책의 ISBN을 입력해주세요.");
                addWish = input.nextLine();
                if (!resultBooks.containsKey(addWish)) {
                    System.out.println("잘못된 isbn입니다.");
                    db.releaseDB(); // 잘못 입력 시 디비 연결 해제 후 종료
                    return;
                }
            }
            db.inputWishList(userid, resultBooks.get(addWish));

            db.releaseDB();
        }

        // 기분에 따른 책 추천
        public void moodBasedBookRecommender(String userid) {
            DBConnect db = new DBConnect();
            db.initDBConnect();

            System.out.println("[오늘은 뭐 읽을까?]");
            System.out.println("😉기분에 따른 도서를 추천해드립니다!\n");
            System.out.println(
                    "모험 | " + "마법 | " + "우정 | " + "신비 | " + "동기부여\n" +
                    "성공 | " + "사랑 | " + "긍정 | " + "가족 | " + "에너지\n" +
                    "역사 | " + "감동 | " + "공감 | " + "사회 | " + "여성\n" +
                    "슬픔 | " + "성장 | " + "철학 | " + "내면 | " + "영감\n"
            );

            Scanner input = new Scanner(System.in);
            System.out.println("오늘의 감정을 입력하세요.");
            String moodInput = input.nextLine();
            System.out.println();
            System.out.print("🔍" + "'"+ moodInput +"'" + " 추천 도서\n");

            String keyword = "";
            switch (moodInput) {
                case "모험":
                    keyword = "모험";
                    break;
                case "마법":
                    keyword = "마법";
                    break;
                case "우정":
                    keyword = "우정";
                    break;
                case "신비":
                    keyword = "신비";
                    break;
                case "동기부여":
                    keyword = "동기부여";
                    break;
                case "성공":
                    keyword = "성공";
                    break;
                case "사랑":
                    keyword = "사랑";
                    break;
                case "긍정":
                    keyword = "긍정";
                    break;
                case "가족":
                    keyword = "가족";
                    break;
                case "에너지":
                    keyword = "에너지";
                    break;
                case "역사":
                    keyword = "역사";
                    break;
                case "감동":
                    keyword = "감동";
                    break;
                case "공감":
                    keyword = "공감";
                    break;
                case "사회":
                    keyword = "사회";
                    break;
                case "여성":
                    keyword = "여성";
                    break;
                case "슬픔":
                    keyword = "슬픔";
                    break;
                case "성장":
                    keyword = "성장";
                    break;
                case "철학":
                    keyword = "철학";
                    break;
                case "내면":
                    keyword = "내면";
                    break;
                case "영감":
                    keyword = "영감";
                    break;
                default:
                    System.out.println("잘못 입력했습니다.");
                    db.releaseDB(); // 잘못 입력 시 디비 연결 해제 후 종료
                    return;
            }

            HashMap<String, Book> recommenderBook = db.moodBook(keyword);

            Iterator<Map.Entry<String, Book>> iterator = recommenderBook.entrySet().iterator();
            int count = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, Book> entry = iterator.next();
                String key = entry.getKey();
                Book book = entry.getValue();
                System.out.println(db.reandomEmoji() + key +
                        " / " + book.getTitle() +
                        " / " + book.getAuthor() +
                        " / " + book.getPublisher() +
                        " / " + book.getCategory() +
                        " / 총" + book.getPages() + "쪽\n" +
                        book.getIntroduce());
                System.out.println("==========================================");
                count++;
                if (count > 5) {
                    break;
                }
            }

            System.out.println("찜 목록에 추가 (Y / N)");
            String yn = input.nextLine();
            String addWish = "";

            if (yn.equalsIgnoreCase("Y")) {
                System.out.println("찜 목록에 추가할 책의 ISBN을 입력해주세요.");
                addWish = input.nextLine();
                if (!recommenderBook.containsKey(addWish)) {
                    System.out.println("잘못된 ISBN입니다.");
                    db.releaseDB(); // 잘못 입력 시 디비 연결 해제 후 종료
                    return;
                }
            }
            db.inputWishList(userid, recommenderBook.get(addWish));

            db.releaseDB();
        }

        // 찜 목록
        public void wishlist(String userid) {
            DBConnect db = new DBConnect();
            db.initDBConnect();

            HashMap<String, Book> wishList = db.selectWishList(userid);

            System.out.println("[내가 찜한 리스트]");
            System.out.println();
            if (wishList.isEmpty()) {
                System.out.println("찜 리스트가 비어 있습니다.\n");
                db.releaseDB(); // 비어 있을 시 디비 연결 해제 후 종료
                return;
            }

            Iterator<Map.Entry<String, Book>> iterator = wishList.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Book> entry = iterator.next();
                String key = entry.getKey();
                Book book = entry.getValue();
                System.out.println(db.reandomEmoji() + key +
                        " / " + book.getTitle() +
                        " / " + book.getAuthor() +
                        " / " + book.getPublisher() +
                        " / " + book.getCategory() +
                        " / 총" + book.getPages() + "쪽\n" +
                        book.getIntroduce());
                System.out.println("==========================================");
            }

            Scanner input = new Scanner(System.in);
            System.out.println("찜 목록에서 삭제하기 (Y / N)");
            String yn = input.nextLine();
            String removeWish = "";

            if (yn.equalsIgnoreCase("N")) {
                db.releaseDB();
                return;
            }

            if (yn.equalsIgnoreCase("Y")) {
                System.out.println("찜 목록에서 삭제할 책의 ISBN을 입력해주세요.");
                removeWish = input.nextLine();
                if (!wishList.containsKey(removeWish)) {
                    System.out.println("잘못된 ISBN입니다.");
                    db.releaseDB(); // 잘못 입력 시 디비 연결 해제 후 종료
                    return;
                }
                db.deleteWishList(userid, wishList.get(removeWish));
            }
            db.releaseDB();
        }
    }

