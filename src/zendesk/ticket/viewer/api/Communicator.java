package zendesk.ticket.viewer.api;

import java.util.ArrayList;

import org.json.simple.JSONObject;

/**
 * @author Shalakha
 *
 */
public interface Communicator {
	public ArrayList<JSONObject> fetchAllTickets();
	public JSONObject fetchTicketById(int ticketId);
	public void initialize();
	public void cleanup();
}
