package company.android.documentmanager.Model;

public class PdfViewState {
    private boolean ScreenState;
   private boolean DisplayState;
   private int pageNumber;
   private boolean bookmarkState;

    public PdfViewState() {
    }

    public PdfViewState(boolean screenState, boolean displayState, int pageNumber, boolean bookmarkState) {
        ScreenState = screenState;
        DisplayState = displayState;
        this.pageNumber = pageNumber;
        this.bookmarkState = bookmarkState;
    }

    public boolean isScreenState() {
        return ScreenState;
    }

    public void setScreenState(boolean screenState) {
        ScreenState = screenState;
    }

    public boolean isDisplayState() {
        return DisplayState;
    }

    public void setDisplayState(boolean displayState) {
        DisplayState = displayState;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isBookmarkState() {
        return bookmarkState;
    }

    public void setBookmarkState(boolean bookmarkState) {
        this.bookmarkState = bookmarkState;
    }
}
