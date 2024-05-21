package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.Serializable;

public record SocketChatComunicationData(boolean getSendedFromAdmin, int getUserID, int getTicketID, String getContent) implements Serializable {
}
