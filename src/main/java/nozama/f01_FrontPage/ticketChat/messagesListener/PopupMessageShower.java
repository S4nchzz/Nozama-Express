package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PopupMessageShower {
    public PopupMessageShower (int ticketID, boolean close) {
        try {
            Socket s = new Socket("127.0.0.1", 25567);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            if (close) {
                out.println("{popUpTicket:" + ticketID + "}." + "close");
            } else {
                out.println("{popUpTicket:" + ticketID + "}");
            }

            s.close();
        } catch (IOException ioe) {

        }
    }
}
