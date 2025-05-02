package DBConnect;

public class Book {
    public String bookid;
    public String title;
    public String author;
    public String publisher;
    public String introduce;
    public String category;
    public String keyword;
    public int pages;

    public Book(String bookid, String title, String author, String publisher, String introduce, String category, String keyword, int pages) {
        this.bookid = bookid;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.introduce = introduce;
        this.category = category;
        this.keyword = keyword;
        this.pages = pages;
    }

    public String getBookid() {
        return bookid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getCategory() {
        return category;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getPages() {
        return pages;
    }
}
