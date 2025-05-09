public class ResultReadBook {
    private String readTime;
    private int readPages;

    public ResultReadBook(String readTime, int readPages) {
        this.readTime = readTime;
        this.readPages = readPages;
    }

    public String getReadTime() {
        return readTime;
    }

    public int getReadPages() {
        return readPages;
    }
}
