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

    public static void addChat(ChatBoxController chat) {
        chats.add(chat);
    }

    public static void delChat(ChatBoxController chat) {
        chats.remove(chat);
    }

    public static ArrayList<ChatBoxController> getChats() {
        return chats;
    }
}