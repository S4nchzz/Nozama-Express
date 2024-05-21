package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class PopupMessageShower implements Serializable {
    private final int ticketID;
    private final boolean notif;

    public PopupMessageShower (int ticketID, boolean notify) {
        this.ticketID = ticketID;
        this.notif = notify;

        try {
            Socket s = new Socket("127.0.0.1", 25566);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(this);
            out.close();
            s.close();
        } catch (IOException ioe) {

        }
    }

    public int getTicketID() {
        return this.ticketID;
    }

    public boolean getClosed() {
        return this.notif;
    }
}
