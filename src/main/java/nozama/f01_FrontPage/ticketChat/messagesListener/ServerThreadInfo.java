package nozama.f01_FrontPage.ticketChat.messagesListener;

public class ServerThreadInfo {
    public static boolean serverThreadRunning;

    public static void setServerThreadRunning (boolean newState) {
        serverThreadRunning = newState;
    }

    public static boolean getServerThreadRunning () {
        return serverThreadRunning;
    }
}
