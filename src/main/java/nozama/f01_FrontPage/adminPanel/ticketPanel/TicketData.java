package nozama.f01_FrontPage.adminPanel.ticketPanel;
public class TicketData {
    private int ticket_id;
    private boolean status;
    private String ticket_type;
    private int solicitante_id;
    private int respondente_id;
    private String problem_desc;
    private String problem_response;
    
    public TicketData(int ticket_id, boolean status, String ticket_type, int solicitante_id, int respondente_id,
            String problem_desc, String problem_response) {
        this.ticket_id = ticket_id;
        this.status = status;
        this.ticket_type = ticket_type;
        this.solicitante_id = solicitante_id;
        this.respondente_id = respondente_id;
        this.problem_desc = problem_desc;
        this.problem_response = problem_response;
    }
    
    public int getTicket_id() {
        return ticket_id;
    }

    public boolean isStatus() {
        return status;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public int getSolicitante_id() {
        return solicitante_id;
    }

    public int getRespondente_id() {
        return respondente_id;
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public String getProblem_response() {
        return problem_response;
    }
}
