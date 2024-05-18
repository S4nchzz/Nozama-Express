package nozama.f01_FrontPage.ticketChat;

public class MessageData {
    private final int message_id;
    private final int ticket_id;
    private final int sender_id;
    private final String sender_Role;
    private final String message;
    private final String date;
    
    public MessageData(int message_id, int ticket_id, int sender_id, String sender_Role, String message, String date) {
        this.message_id = message_id;
        this.ticket_id = ticket_id;
        this.sender_id = sender_id;
        this.sender_Role = sender_Role;
        this.message = message;
        this.date = date;
    }
    
    public int getMessage_id() {
        return message_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public String getSender_Role() {
        return sender_Role;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
