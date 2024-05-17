package nozama.f01_FrontPage.chat;

import java.util.ArrayList;

public class CentralizedChats {
    private final ArrayList<ChatBoxController> chats;
    
    public CentralizedChats () {
        this.chats = new ArrayList<>();
    }

    public void addChat(ChatBoxController chat) {
        this.chats.add(chat);
    }

    public void delChar(ChatBoxController chat) {
        this.chats.remove(chat);
    }
}
