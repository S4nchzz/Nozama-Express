package nozama.f01_FrontPage.chat;

import java.util.ArrayList;

public class CentralizedChats {
    private final ArrayList<ChatBoxController> chats;
    private static CentralizedChats centralized = new CentralizedChats();

    private CentralizedChats() {
        this.chats = new ArrayList<>();
    }

    public static CentralizedChats getInstance() {
        return centralized;
    }

    public void addChat(ChatBoxController chat) {
        this.chats.add(chat);
    }

    public void delChar(ChatBoxController chat) {
        this.chats.remove(chat);
    }

    public ArrayList<ChatBoxController> getChats() {
        return this.chats;
    }
}