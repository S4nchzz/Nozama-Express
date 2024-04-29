package nozama.f01_FrontPage.chat.messagesListener;

import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

public class MessageListener {
    private static final MessageListener instance = new MessageListener ();
    private Map<Integer, Set<Notify>> map;

    public static MessageListener getInstance() {
        return instance;
    }

    private MessageListener () {
        this.map = new TreeMap <Integer, Set<Notify>> ();
    }

    public void registerToNotify(int ticketID, Notify notify) {
        // se registra si no lo estaba ya
        Set<Notify> r = map.get (ticketID);

        if (r == null) {
            r = new TreeSet<Notify> ();
            map.put (ticketID, r);
        }

        r.add (notify);
    }

    public void desregistrar (int ticketID, Notify notify) {
        // se desregistra si es llamado
        Set<Notify> r = map.get(ticketID);

        if (r != null) {
            r.remove (notify);
        }
    }

    public void avisar (int ticketID) {
        final Set<Notify> quienes = map.get (ticketID);

        if (quienes != null) {
            for (Notify n : quienes) {
                n.avisar ();
            }
        }
    }
}
