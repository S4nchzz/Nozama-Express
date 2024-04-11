package nozama.f01_FrontPage.adminPanel.tables.support;

import javafx.beans.property.SimpleStringProperty;

public class TableDataSupport {
    private SimpleStringProperty ticket_id;
    private SimpleStringProperty ticket_status;
    private SimpleStringProperty ticket_type;
    private SimpleStringProperty solicitante_id;
    private SimpleStringProperty respondente_id;
    private SimpleStringProperty problem_desc;
    private SimpleStringProperty problem_response;
    
    public TableDataSupport(int ticket_id, String ticket_status, String ticket_type, String solicitante_id,
            String respondente_id, String problem_desc, String problem_response) {
        this.ticket_id = new SimpleStringProperty(String.valueOf(ticket_id));
        this.ticket_status = new SimpleStringProperty(ticket_status);
        this.ticket_type = new SimpleStringProperty(ticket_type);
        this.solicitante_id = new SimpleStringProperty(solicitante_id);
        this.respondente_id = new SimpleStringProperty(respondente_id);
        this.problem_desc = new SimpleStringProperty(problem_desc);
        this.problem_response = new SimpleStringProperty(problem_response);
    }

    public String getTicket_id() {
        return ticket_id.get();
    }

    public String getTicket_status() {
        return ticket_status.get();
    }

    public String getTicket_type() {
        return ticket_type.get();
    }

    public String getSolicitante_id() {
        return solicitante_id.get();
    }

    public String getRespondente_id() {
        return respondente_id.get();
    }

    public String getProblem_desc() {
        return problem_desc.get();
    }

    public String getProblem_response() {
        return problem_response.get();
    }
}