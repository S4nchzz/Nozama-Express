package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.util.ArrayList;

public class CentralizedTicketTemplates {
    private static CentralizedTicketTemplates centralizedTicketsInstance = new CentralizedTicketTemplates();
    private static ArrayList<TicketElement> centralizedTickets = new ArrayList<>();

    private CentralizedTicketTemplates () {

    }

    public static CentralizedTicketTemplates getInstance () {
        return centralizedTicketsInstance;
    }

    public static void addTicketTemplate(TicketElement template) {
        centralizedTickets.add(template);
    }

    public static void delTicketTemplate(int ticket_id) {
        for (TicketElement ticket : CentralizedTicketTemplates.centralizedTickets) {
            if (ticket.getTicketData().getTicket_id() == ticket_id) {
                centralizedTickets.remove(ticket);
            }
        }
    }

    public static ArrayList<TicketElement> getChats() {
        return centralizedTickets;
    }

}
