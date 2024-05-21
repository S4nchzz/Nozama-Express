package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class AdminSocket implements Serializable {
    public AdminSocket (SocketChatComunicationData socketMessageData) {
        try {
            Socket s = new Socket("127.0.0.1", 25566);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(socketMessageData);
            out.close();
            s.close();
        } catch (IOException e) {
        
        }
    }
}
