package company.android.documentmanager.BottomAction;

public class ActionBox {
    private boolean screenRotate;
    private boolean nightMode;
    private int pageNumber;
    private boolean numberChangeFromMe;

    public ActionBox(boolean screenRotate, boolean nightMode, int pageNumber, boolean numberChangeFromMe) {
        this.screenRotate = screenRotate;
        this.nightMode = nightMode;
        this.pageNumber = pageNumber;
        this.numberChangeFromMe = numberChangeFromMe;
    }

    public boolean isScreenRotate() {
        return screenRotate;
    }

    public void setScreenRotate(boolean screenRotate) {
        this.screenRotate = screenRotate;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isNumberChangeFromMe() {
        return numberChangeFromMe;
    }

    public void setNumberChangeFromMe(boolean numberChangeFromMe) {
        this.numberChangeFromMe = numberChangeFromMe;
    }
}
