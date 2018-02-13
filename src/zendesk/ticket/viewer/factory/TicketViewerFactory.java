package zendesk.ticket.viewer.factory;

import zendesk.ticket.viewer.api.Communicator;
import zendesk.ticket.viewer.api.TicketViewer;
import zendesk.ticket.viewer.impl.CommunicatorImpl;
import zendesk.ticket.viewer.impl.TicketViewerImpl;

/**
 * @author Shalakha
 *
 */
public class TicketViewerFactory {
	public static TicketViewer getViewer(){
		return new TicketViewerImpl();
	}
	public static Communicator getCommunicator(){
		CommunicatorImpl comm = new CommunicatorImpl();
		comm.initialize();
		return comm;
	}
}
