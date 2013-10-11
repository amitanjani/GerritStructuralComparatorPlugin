package com.tkd.google;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
//import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.gdata.client.Query;
import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.calendar.*;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.*;
import com.google.gdata.data.extensions.Who.AttendeeStatus;
import com.google.gdata.data.extensions.Who.AttendeeType;
import com.google.gdata.util.ServiceException;
import com.sun.org.apache.xml.internal.security.utils.Base64; 
//import com.google.gdata.util.common.util.Base64;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.LinkedList;
import java.util.List;

public class GoogleUtility {
	public static void main(String[] args) throws Exception{
//		File file = new File("/home/tapan/logs/out.txt");
//		FileOutputStream fos = new FileOutputStream(file);
//		PrintStream ps = new PrintStream(fos);
//		System.setOut(ps);
		
		GoogleUtility gUtil = new GoogleUtility();
		gUtil.authenticateImaginea();
		//gUtil.addEvent();
		
		//gUtil.updateEvent();
		//gUtil.deleteEvent();
		//gUtil.searchEventsByDateRange("2013-05-02T16:30:00-08:00", "2013-05-02T17:30:00-08:00");
		gUtil.searchEventsByText("a");
	}
	
	/*GoogleUtility starts here*/
	
	public void authenticateImaginea() throws ServiceException, IOException, OAuthException, ServletException{
		String CONSUMER_KEY 	= "imaginea.com";
		String CONSUMER_SECRET 	= "mmca_dvcEjt0VFcXWD4Pg59L";
		String SCOPE        	= "https://www.googleapis.com/auth/calendar";
		//String SCOPE        	= "https://www.google.com/calendar/feeds/";
		String REFRESH_TOKEN 	= "lWxe38u6eTmZmGaQEwpxubQe9k2ZoUQHo4v2ebykiCfsrQ4sYzpPjOxgwEfZ/2ZC";
		String AUTH_CODE 		= "DQAAANoBAADNxOv-aMUQB_UEXQ18ZIBjlD3O_OmlW2EGKw_XbjiMuSeKC3arzDKxXgaYbKLTVobW3qJqn-EEWw4iwQ_oh5yW2iWhIRebnvYLr24Wu5Rnp6FCTCM5XlC-A6ehP1BpV0xrC7CcGXyJ8QfbP08xU174_CngQJ0QgrK50sa7kijtZTjqWYxKAk3uiUIhJKTilOdkrO41580CP0wIUfSER6bFYyqFm7gb1XfotvSvWFe1KajFlueMfH2q1l3Irzj8uq8LzbYesKgahRSr8gIGB2kJFBujQW_76YG5C2LFU2iMnTcjEtm5qenacqkVTf7tsF4vJAaQSexeK0RvgZ5c6QZI_MMkoH0n6uEOfgdM5W5jNcfwJMKnfKKfTWCLBiQEAOkxzMp2g5PNPBexmsu6Ozx7ZmGyjx0LVfENU2q0jT80oFIkKC7kdSLClQ5g8d9mJsDwGTiQvzuJROwuOa8yUf6C_VA_2FA0ExBBiGj7COMdNIEmup9hkn2CNcbTtRgQXBTEUV2hJNd5s7fY8nZK-GIUTr0RFWEFFRGd7iizSlLvWx-AIH0MRJbmKkJgCmCQ0Tq-XkctOYYsXmfJov7DKJffk0Sal6XIv5NE2YGweXjNOIRSxEK3HZ7elXUMY1v0N9U";
		
		String ADMIN_SECRET = "+6Bj6wtudoNPI269sloADw==";
		
		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
		oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
		oauthParameters.setOAuthTokenSecret(AUTH_CODE);
		//oauthParameters.setOAuthVerifier(authCode);
		//oauthParameters.setScope(SCOPE);
		
		//accessToken = getAccessToken(REFRESH_TOKEN, CONSUMER_KEY, CONSUMER_SECRET);
		//oauthParameters.setOAuthToken(REFRESH_TOKEN);
		oauthParameters.setOAuthType(GoogleOAuthParameters.OAuthType.TWO_LEGGED_OAUTH);

    	OAuthHmacSha1Signer signer = new OAuthHmacSha1Signer();

    	
        try {
        	calService = new CalendarService("Test-Imaginea");
        	calService.setOAuthCredentials(oauthParameters, signer);
        } catch (Exception e) {
            throw new ServletException("Unable to initialize calendar service", e);
        }

        
//	    HttpTransport transport         = new NetHttpTransport();
//        JsonFactory jsonFactory         = new JacksonFactory();
//
//		GoogleCredential credentials = new GoogleCredential.Builder()
//	    .setClientSecrets(CONSUMER_KEY, CONSUMER_SECRET)
//	    .setJsonFactory(jsonFactory).setTransport(transport).build()
//	    .setRefreshToken(REFRESH_TOKEN);

//    	GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);
		//oauthHelper.getUnauthorizedRequestToken(oauthParameters);
		
//		accessToken = oauthHelper.getAccessToken(oauthParameters);
//	    System.out.println("OAuth Access Token: " + accessToken);
//		oauthParameters.setOAuthToken(accessToken);

		
//		oauthParameters.setOAuthCallback("https://apps-apis.google.com/a/feeds/calendar/resource/2.0/imaginea.com/");
		//String accessToken = getAccessToken(REFRESH_TOKEN, CONSUMER_KEY, CONSUMER_SECRET);
		
//		String requestUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);
//	    System.out.println(requestUrl);

	    
//	    System.out.println("Please visit the URL above to authorize your OAuth "
//	        + "request token.  Once that is complete, press any key to "
//	        + "continue...");
//		    
//	    String token = oauthHelper.getAccessToken(oauthParameters);
//	    System.out.println("OAuth Access Token: " + token);
//	    System.out.println();

		
		
//		CalendarService service = new CalendarService("mmca_dvcEjt0VFcXWD4Pg59L");  
//		service.setUserCredentials("netadmin@imaginea.com", "+6Bj6wtudoNPI269sloADw==");  
//	
//		URL feedUrl = new URL(String.format("http://www.google.com/calendar/feeds/{0}/owncalendars/full", "tapan.d@imagimea.com"));
//		CalendarQuery myQuery = new CalendarQuery(feedUrl);
//		EventFeed eventFeed = calService.query(myQuery, EventFeed.class);		
//		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
//			EventEntry evtEntry = eventFeed.getEntries().get(i);
//			System.out.println(evtEntry.getId() + " $ " + evtEntry.getAuthors().get(0).getName() + " $ " +  evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
//		}
	}
	
	private CalendarService calService = null;
	private Person author = null;
    //private String calEmail = "tjq21kv7pu92k29oetv03dv4cc@group.calendar.google.com"; //tapank156
    //private String calEmail = "imaginea.com_3530383431323437343833@resource.calendar.google.com" //venu
    private String calEmail = "imaginea.com_5dducap8sap04m5s5rt4je1eu4@group.calendar.google.com"; //"netadmin@imaginea.com"; //netadmin
    //private String calEmail = "netadmin@imaginea.com"; //netadmin
    
	//private String calEmail = "imaginea.com_bmk59675u061el36j446kmea38@group.calendar.google.com"; //tapan.d
    public GoogleUtility()throws IOException, ServletException{
		this.calService = authenticate();	
		//this.author = new Person("Tapan Kumar", null, "tapank156@gmail.com");
		this.author = new Person("Tapan Kumar", null, "tapan.d@imaginea.com");
		//this.author = new Person("Imaginea Admin", null, "netadmin@imaginea.com");
	}

	public GoogleUtility(CalendarService calService)throws IOException, ServletException{
		this.calService = calService;	
		//this.author = new Person("Tapan Kumar", null, "tapank156@gmail.com");
		this.author = new Person("Tapan Kumar", null, "tapan.d@imaginea.com");
		//this.author = new Person("Imaginea Admin", null, "netadmin@imaginea.com");
	}
	
	public CalendarService getCalService() {
		return calService;
	}

	public void addEventNew(String email)throws IOException, ServiceException{
		URL postUrl = new URL("http://www.google.com/calendar/feeds/"+email+"/private/full");
		EventEntry evtEntry = new EventEntry();
		evtEntry.setTitle(new PlainTextConstruct("Apps Portal Discussion"));
		evtEntry.setContent(new PlainTextConstruct("Discussion to integrate event booking in Qb and Goole Calendar through Apps Portal."));
		
		List<Who> participants = evtEntry.getParticipants();		
		Who who = new Who();
		who.setAttendeeStatus(AttendeeStatus.EVENT_INVITED);
		who.setAttendeeType(AttendeeType.EVENT_REQUIRED);
		who.setEmail("tkdinda@gmail.com");
		who.setValueString("Tapan Dinda");
		participants.add(who);

		DateTime startTime = DateTime.parseDateTime("2013-05-02T16:30:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2013-05-02T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		evtEntry.addTime(eventTimes);
		evtEntry.addLocation(new Where("","","Midtown, Hyderabad"));

		EventEntry retEntry = calService.insert(postUrl, evtEntry);
		System.out.println(retEntry.getHtmlLink().getHref());		
	}
	
	public void addEvent()throws IOException, ServiceException{
		URL postUrl =  new URL("http://www.google.com/calendar/feeds/"+ calEmail +"/private/full");		
		EventEntry evtEntry = new EventEntry();
		evtEntry.setTitle(new PlainTextConstruct("Apps Portal Discussion"));
		evtEntry.setContent(new PlainTextConstruct("Discussion to integrate event booking in Qb and Goole Calendar through Apps Portal."));
		List<Person> authors = evtEntry.getAuthors();
		authors.add(author);
		
//		List<Who> participants = evtEntry.getParticipants();		
//		Who who = new Who();
//		who.setAttendeeStatus(AttendeeStatus.EVENT_INVITED);
//		who.setAttendeeType(AttendeeType.EVENT_REQUIRED);
//		who.setEmail("tkdinda@gmail.com");
//		who.setValueString("Tapan Dinda");
//		participants.add(who);

		DateTime startTime = DateTime.parseDateTime("2013-05-09T16:30:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2013-05-09T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		evtEntry.addTime(eventTimes);
		evtEntry.addLocation(new Where("","","Midtown, Hyderabad"));
		EventEntry retEntry = calService.insert(postUrl, evtEntry);
		System.out.println(retEntry.getHtmlLink().getHref());		
	}
	
	public void updateEvent()throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+calEmail + "/private/full");
		EventFeed eventFeed = calService.getFeed(feedUrl, EventFeed.class);		
		EventEntry evtEntry = eventFeed.getEntries().get(0);
		evtEntry.setTitle(new PlainTextConstruct("Update:Apps Portal Discussion"));
		evtEntry.setContent(new PlainTextConstruct("Discussion to integrate event booking in QB and Goole Calendar through Apps Portal and QB changes accordingly."));
		List<Person> authors = evtEntry.getAuthors();
		authors.add(author);
		List<Who> participants = evtEntry.getParticipants();		
		Who who = new Who();
		who.setAttendeeStatus(AttendeeStatus.EVENT_INVITED);
		who.setAttendeeType(AttendeeType.EVENT_REQUIRED);
		who.setEmail("tapan.d@imaginea.com");
		who.setValueString("Tapan Dinda");
		participants.add(who);
		
		DateTime startTime = DateTime.parseDateTime("2013-05-02T16:30:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2013-05-02T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		evtEntry.addTime(eventTimes);
		evtEntry.addLocation(new Where("","","Midtown, Hyderabad, AP"));

		EventEntry retEntry = evtEntry.update();
		System.out.println(retEntry.getHtmlLink().getHref());		
	}

	public void deleteEvent()throws IOException, ServiceException{
		URL feedUrl =  new URL("http://www.google.com/calendar/feeds/"+calEmail+"/private/full");
		EventFeed eventFeed = calService.getFeed(feedUrl, EventFeed.class);

		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			EventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getTimes());
			evtEntry.delete();
		}
	}

	public void searchEventsByDateRange(String startDate, String endDate)throws IOException, ServiceException{

		URL feedUrl = new URL("https://www.google.com/calendar/feeds/"+ calEmail+"/private/full");
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		myQuery.setMinimumStartTime(DateTime.parseDateTime(startDate));
		myQuery.setMaximumStartTime(DateTime.parseDateTime(endDate));
	
		// Send the request and receive the response:
		EventFeed eventFeed = calService.query(myQuery, EventFeed.class);
		
		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			EventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
		}
	}
	public void searchEventsByText(String content)throws IOException, ServiceException{		
		URL feedUrl = new URL("https://www.google.com/calendar/feeds/"+calEmail+"/private/full");
		//URL feedUrl = new URL("https://apps-apis.google.com/a/feeds/calendar/resource/2.0/imaginea.com/");
		
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		if(content !=null) myQuery.setFullTextQuery(content);	
		// Send the request and receive the response:		
		System.out.println(feedUrl);
		
		myQuery.addCustomParameter(new Query.CustomParameter("xoauth_requestor_id", "netadmin@imaginea.com"));
		
		EventFeed eventFeed = calService.query(myQuery, EventFeed.class);
		
		for (int i = 0; i<eventFeed.getEntries().size(); i++) {
			EventEntry evtEntry = eventFeed.getEntries().get(i);
			System.out.println(evtEntry.getTitle().getPlainText() + " on " + evtEntry.getPublished());
		}
	}
	String passpwrd = "????";
	private CalendarService authenticate() throws IOException, ServletException {
		CalendarService service = null; 
		try {
//			service = new CalendarService("Test-Imaginea");
//			service.setUserCredentials("tapan.d@imaginea.com", "ganapati@0i2");
			
//			service.setUserCredentials("netadmin@imaginea.com", passpwrd);

			
			//service = new CalendarService("TestAppl");
			//service.setUserCredentials("tapank156@gmail.com", "systemedge");

			//service = new CalendarService("Test-Apps");
			//service.setUserCredentials("tapan.d@imaginea.com", "ganapati@0i2");
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 	
		return service;
	}
}

/*
try 
{
    final OAuthService oauth = OAuthServiceFactory.getOAuthService();
    final User user = oauth.getCurrentUser();
}
catch (final OAuthRequestException e)
{
    throw new RuntimeException(e);
}
	
	public String getAccessToken(String clientId, String clientSecret, String scope, String code) throws IOException{
		 HttpTransport httpTransport = new NetHttpTransport();
		    JacksonFactory jsonFactory = new JacksonFactory();
		    // Or your redirect URL for web based applications.
		    String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
		    GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory,
		        clientId, clientSecret, code, redirectUrl).execute();
		    return response.getAccessToken();
	}
	
	public static String getAccessToken(String refreshToken, String client_id, String client_secret) throws IOException {
	    HttpTransport transport = new NetHttpTransport();
	    JsonFactory jsonFactory = new JacksonFactory();
	    //String redirect_uri     = "https://www.imaginea.com/oauth2callback";
	    GoogleRefreshTokenRequest req = new GoogleRefreshTokenRequest(transport, jsonFactory, refreshToken, client_id, client_secret);
	    String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
	    GoogleTokenResponse res = req.execute();
	    String accessToken = res.getAccessToken();

	    return accessToken;
	    
	}
	public String getAccessToken(String code, String client_id, String client_secret, boolean a) throws IOException {
	    HttpTransport transport         = new NetHttpTransport();
        JsonFactory jsonFactory         = new JacksonFactory();
		//String scope        	= "https://www.google.com/calendar/feeds/";
	    String scope = "https://www.googleapis.com/auth/calendar";
		List <String> scopes = new LinkedList<String>();
	    scopes.add(scope);
	    String redirect_uri = "https://www.imaginea.com/oauth2callback";
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, client_id, client_secret, scopes).build();
	    GoogleTokenResponse res = flow.newTokenRequest(code).setRedirectUri(redirect_uri).execute();
	    String refreshToken = res.getRefreshToken();
	    String accessToken = res.getAccessToken();
	    return accessToken;
	}
	
	public void authUrl(){
		String hostedDomain = "imaginea.com";
		String nextUrl = "https://www.imaginea.com/welcome.jsp";
	    String scope = "https://www.googleapis.com/auth/calendar";
		boolean secure = true;  // set secure=true to request AuthSub tokens
		boolean session = true;
		String authSubUrl = AuthSubUtil.getRequestUrl(hostedDomain, nextUrl, scope, secure, session);
		System.out.println(authSubUrl);
	}

*/