package nozama.f01_FrontPage.ticketChat;

import java.util.ArrayList;

public class CentralizedChats {
    private static final ArrayList<ChatBoxController> chats = new ArrayList<>();
    private static CentralizedChats centralized = new CentralizedChats();

    private CentralizedChats() {
    
    }

    public static CentralizedChats getInstance() {
        return centralized;
    }

    public void addChat(ChatBoxController chat) {
        chats.add(chat);
    }

    public void delChat(ChatBoxController chat) {
        chats.remove(chat);
    }

    public ArrayList<ChatBoxController> getChats() {
        return chats;
    }
}