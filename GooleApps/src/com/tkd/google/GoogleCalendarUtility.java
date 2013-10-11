package com.tkd.google;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.ServiceException;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class GoogleCalendarUtility {
	public static void main(String[] args) throws Exception{
		GoogleCalendarUtility gUtil = new GoogleCalendarUtility();
		//GoogleUtility gUtil = new GoogleUtility();
		//gUtil.addEvent();
		gUtil.updateEvent();
		//gUtil.deleteEvent();
		gUtil.searchEventsByDateRange("2013-05-02T16:30:00-08:00", "2013-05-02T17:30:00-08:00");
		gUtil.searchEventsByText("lesson");
	}

	/*GoogleCalendarUtility starts here*/
	private CalendarService calService = null;
	private Person author = null;
    private String calEmail = "imaginea.com_bmk59675u061el36j446kmea38@group.calendar.google.com";
	public GoogleCalendarUtility()throws IOException, ServletException{
		this.calService = authenticate();	
		this.author = new Person("Jo March", null, "tapank156@gmail.com");
	}
	
	private CalendarService authenticate() throws IOException, ServletException {
		CalendarService myService = null; 
		try {
	        myService = new CalendarService("TestAppl");
	        myService.setUserCredentials("tapank156@gmail.com", "systemedge");

	    } catch (Exception e) {
	        e.printStackTrace();
	    } 	
		return myService;
	}

	public void addEvent()throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");
		CalendarEventEntry evtEntry = new CalendarEventEntry();
		evtEntry.setTitle(new PlainTextConstruct("Tennis with Beth"));
		evtEntry.setContent(new PlainTextConstruct("Meet for a quick lesson with him."));
		evtEntry.getAuthors().add(author);
		DateTime startTime = DateTime.parseDateTime("2013-05-02T16:30:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2013-05-02T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		evtEntry.addTime(eventTimes);
		evtEntry.addLocation(new Where("","","Midtown, Hyderabad"));
		CalendarEventEntry retEntry = calService.insert(feedUrl, evtEntry);
		System.out.println(retEntry.getHtmlLink().getHref());		
	}
	
	public void updateEvent()throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");
		CalendarEventFeed eventFeed = calService.getFeed(feedUrl, CalendarEventFeed.class);		
		CalendarEventEntry evtEntry = eventFeed.getEntries().get(0);
		evtEntry.setTitle(new PlainTextConstruct("Tennis with Beth"));
		evtEntry.setContent(new PlainTextConstruct("Meet for a quick sesssion with him."));
		evtEntry.getAuthors().add(author);
		DateTime startTime = DateTime.parseDateTime("2013-05-02T16:30:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2013-05-02T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		evtEntry.addTime(eventTimes);
		evtEntry.addLocation(new Where("","","Midtown, Hyderabad, AP"));

		CalendarEventEntry retEntry = evtEntry.update();
		System.out.println(retEntry.getHtmlLink().getHref());		
	}

	public void deleteEvent()throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");
		CalendarEventFeed eventFeed = calService.getFeed(feedUrl, CalendarEventFeed.class);

		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			CalendarEventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
			evtEntry.delete();
		}
	}

	public void searchEventsByDateRange(String startDate, String endDate)throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		myQuery.setMinimumStartTime(DateTime.parseDateTime(startDate));
		myQuery.setMaximumStartTime(DateTime.parseDateTime(endDate));
	
		// Send the request and receive the response:
		CalendarEventFeed eventFeed = calService.query(myQuery, CalendarEventFeed.class);
		
		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			CalendarEventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
		}
	}
	
	public void searchEventsByText(String content)throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		if(content !=null) myQuery.setFullTextQuery(content);	
		// Send the request and receive the response:
		CalendarEventFeed eventFeed = calService.query(myQuery, CalendarEventFeed.class);
		
		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			CalendarEventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
		}
	}
    //web part
	private String appName = "TestAppl";
	private String access_token = "AIzaSyAbvmwpPGzfcAX853f4SLsWdQpM8R-4bPo";
	private String refreshToken = "yyyyy";
	private String client_id    = "1011307938665-24gh1m0c5lmdpgiojq0i3vvfkdut815f.apps.googleusercontent.com";
	private String redirect_uri = "http://localhost:8088/oauth2callback";
	private String scope        = "https://www.googleapis.com/auth/calendar";
	private String client_secret= "dFTP44-uSvQNirQ87qAzIOUK";

	
	public void authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpTransport transport         = new NetHttpTransport();
		JsonFactory jsonFactory         = new JacksonFactory();

		List <String> scopes = new LinkedList<String>();
	    scopes.add(scope);
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, client_id, client_secret, scopes).build();
	    GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
	    url.setRedirectUri(redirect_uri);
	    url.setApprovalPrompt("force");
	    url.setAccessType("offline");
	    String authorize_url = url.build();
	    response.sendRedirect(authorize_url);
	}
	
	public void importCalendarList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    HttpSession session = request.getSession();
	    String staffKey = (String) session.getAttribute("staffKey");
	   // ContactJdo staffDetails = staff.getStaffDetail(staffKey);
	    String code = request.getParameter("code");
	    HttpTransport transport         = new NetHttpTransport();
        JsonFactory jsonFactory         = new JacksonFactory();
        String scope = "https://www.googleapis.com/auth/plus.me";
		List <String> scopes = new LinkedList<String>();
	    scopes.add(scope);

	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, client_id, client_secret, scopes).build();
	    GoogleTokenResponse res = flow.newTokenRequest(code).setRedirectUri(redirect_uri).execute();
	    String refreshToken = res.getRefreshToken();
	    String accessToken = res.getAccessToken();

	    List <CalendarEventEntry> evtList = null; //getCalendars(accessToken);

	    for(CalendarEventEntry entry : evtList) {
	        System.out.println(entry.getId());
	    }
	}

	public static String getAccessToken(String refreshToken, String client_id, String client_secret) throws IOException {
	    HttpTransport transport         = new NetHttpTransport();
	    JsonFactory jsonFactory         = new JacksonFactory();

	    GoogleRefreshTokenRequest req = new GoogleRefreshTokenRequest(transport, jsonFactory, refreshToken, client_id, client_secret);
	    GoogleTokenResponse res = req.execute();
	    String accessToken = res.getAccessToken();
	    return accessToken;
	}
}
