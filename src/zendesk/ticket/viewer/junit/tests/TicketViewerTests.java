package zendesk.ticket.viewer.junit.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.junit.Test;

import zendesk.ticket.viewer.impl.CommunicatorImpl;
import zendesk.ticket.viewer.impl.TicketViewerImpl;

public class TicketViewerTests {

	@Test
	public void testExceptionFetchFalseTicketID(){
		TicketViewerImpl tktV = new TicketViewerImpl();		
		try{
			tktV.viewTicketById(Integer.parseInt("-hsgdkshdkjs878"));			
		}catch(Exception e){
			assertNotNull(e);
			assertEquals(e.getClass(), Exception.class);
		}
	}
	
	@Test
	public void testFetchTicketByID(){
		int id = 1;
		CommunicatorImpl comm = new CommunicatorImpl();
		comm.initialize();
		JSONObject ticket = new JSONObject();
		ticket  = comm.fetchTicketById(id);
		assertEquals(1, ticket.get("id"));	
	}
	
	@Test
	public void testFetchAllTickets(){
		CommunicatorImpl comm = new CommunicatorImpl();
		comm.initialize();
		ArrayList<JSONObject> fetchAllTickets = new ArrayList<JSONObject>();
		try{
			fetchAllTickets = comm.fetchAllTickets();			
		}catch(Exception e){
			assertNotNull(e);
			assertEquals(e.getClass(), Exception.class);
		}	
	}	
}
