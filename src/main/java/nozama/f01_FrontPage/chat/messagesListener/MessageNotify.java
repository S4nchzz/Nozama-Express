package nozama.f01_FrontPage.chat.messagesListener;

public class MessageNotify implements Notify {
    public void avisar() {
        MessageListener messageListener = MessageListener.getInstance();
        // Buscar ticket que tenga un nuevo mensaje
        messageListener.registerToNotify(0, null);
    }
}