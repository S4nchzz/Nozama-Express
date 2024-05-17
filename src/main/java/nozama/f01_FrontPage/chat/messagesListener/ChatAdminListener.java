package nozama.f01_FrontPage.chat.messagesListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatAdminListener {
    public ChatAdminListener (String content) {
        try {
            Socket s = new Socket("127.0.0.1", 25567);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("{admin}" + content + "\n");
            s.close();
        } catch (IOException e) {
        
        }
    }
}
