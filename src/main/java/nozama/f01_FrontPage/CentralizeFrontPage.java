package nozama.f01_FrontPage;

import java.util.ArrayList;

public class CentralizeFrontPage {
    private static ArrayList<FrontPage> frontPageElements = new ArrayList<>();
    private static CentralizeFrontPage central = new CentralizeFrontPage();

    private CentralizeFrontPage () {

    }

    public static CentralizeFrontPage getInstance() {
        return central;
    }

    public void addFrontPage(FrontPage frontPage) {
        frontPageElements.add(frontPage);
    }

    public void delFrontPage(FrontPage frontPage) {
        frontPageElements.remove(frontPage);
    }

    public ArrayList<FrontPage> getFrontPageElements() {
        return frontPageElements;
    }
}
