import DBConnect.DBShop;
import DBConnect.User;
import DBConnect.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Shop {

    public void shopManager(User currentUser){
        while (true){
            System.out.println("[💰포인트샵]");
            System.out.println("1. 책 구매하기");
            System.out.println("2. 구매 내역 조회");
            System.out.println("3. 메뉴로 가기");
            System.out.println();
            System.out.print("메뉴선택 : ");

            Scanner s = new Scanner(System.in);
            int select = s.nextInt();
            s.nextLine();
            switch (select){
                case 1:
                    this.pointShop(currentUser.getUserId(), currentUser.getAddr());
                    break;
                case 2:
                    this.showOrders(currentUser.getUserId());
                    break;
                case 3:
                    System.out.println();
                    System.out.println("메인 메뉴로 이동\n");
                    return;
            }
        }

    }
    public void pointShop(String userid, String userAddr){
        DBShop db = new DBShop();
        db.initDBConnect();
        HashMap<String, Book> wishlist = db.selectWishList(userid);
        if(wishlist.isEmpty()){
            System.out.println("찜 리스트가 비어있습니다.");
            return;
        }
        int points = db.getUserPoints(userid);
        System.out.println();
        System.out.println("[🛒책 구매하기]\n");
        System.out.println(userid + "님 보유 북싹포인트 : " + points);
        System.out.println();
        Iterator<Map.Entry<String, Book>> iterator = wishlist.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Book> entry = iterator.next();
            System.out.print(db.reandomEmoji() + entry.getValue().getBookid() + " / ");
            System.out.print(entry.getValue().getTitle() + " / ");
            System.out.print(entry.getValue().getAuthor() + " / ");
            System.out.print(entry.getValue().getPublisher() + " / ");
            System.out.println(entry.getValue().getPages()+"쪽");
            System.out.println(entry.getValue().getIntroduce());
            System.out.println("💰필요 북싹포인트 : " +entry.getValue().getPrice());
            System.out.println("========================================");
        }
        System.out.println("\n구매하실 책의 ISBN을 입력하세요.");
        Scanner s = new Scanner(System.in);
        String inputIsbn = s.nextLine();
        System.out.println();
        if(wishlist.containsKey(inputIsbn)){
            Book book = wishlist.get(inputIsbn);
            System.out.println("💸" + book.getTitle()+"을(를) 구매하시겠습니까? Y | N");
            System.out.print("구매 후 남는 포인트 : ");
            System.out.print(points - book.getPrice() + "\n");
            System.out.print("구매 확인 : ");
            String yn = s.nextLine();
            System.out.println();
            if(yn.toUpperCase().trim().equals("Y")){
                if(book.getPrice() > points){
                    System.out.println("😭포인트가 부족합니다.\n");
                    return;
                }

                System.out.println("1. 주소로 보내기");
                System.out.println("2. 새로운 배송지");
                int select = s.nextInt();
                s.nextLine();
                String addr= "";
                if(select==1){
                    addr = userAddr;
                }
                if(select==2){
                    System.out.print("배송지를 입력해주세요 : ");
                    addr = s.nextLine();
                    System.out.println();
                }
                System.out.println();
                db.buyBook(userid, inputIsbn, points - book.getPrice(), addr);
                System.out.println("📦구매하신 책이 " + addr +" 로(으로) 배송됩니다.");
                System.out.println();
                db.releaseDB();
            }
        }
    }

    public void showOrders(String userid){
        DBShop db = new DBShop();
        db.initDBConnect();
        db.showOrders(userid);
        db.releaseDB();
    }
}
