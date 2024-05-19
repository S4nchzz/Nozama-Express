package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminSocket {
    public AdminSocket (String content, int ticketID, int userID) {
        try {
            Socket s = new Socket("127.0.0.1", 25567);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("{" + userID + ":admin:" + ticketID + "}" + content + "\n");
            s.close();
        } catch (IOException e) {
        
        }
    }
}
