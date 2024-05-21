package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.util.ArrayList;

public class CentralizedTicketElements {
    private static CentralizedTicketElements centralizedTicketsInstance = new CentralizedTicketElements();
    private static ArrayList<TicketElement> centralizedTickets = new ArrayList<>();

    private CentralizedTicketElements () {

    }

    public static CentralizedTicketElements getInstance () {
        return centralizedTicketsInstance;
    }

    public void addTicketTemplate(TicketElement template) {
        centralizedTickets.add(template);
    }

    public void delTicketTemplate(int ticket_id) {
        for (TicketElement ticket : CentralizedTicketElements.centralizedTickets) {
            if (ticket.getTicketData().getTicket_id() == ticket_id) {
                centralizedTickets.remove(ticket);
            }
        }
    }

    public ArrayList<TicketElement> getChats() {
        return centralizedTickets;
    }

}
