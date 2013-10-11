package com.tkd.google;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.ServiceException;

public class GoogleAuthUser {
	String hostedDomain = "imaginea.com";
	String nextUrl = "http://imaginea.com/authorize";
	//String scope = "https://www.google.com/calendar/feeds/";
	boolean secure = false;  // set secure=true to request AuthSub tokens
	boolean session = true;
	private String appName = "TestAppl";
	private String access_token = "AIzaSyAbvmwpPGzfcAX853f4SLsWdQpM8R-4bPo";
	private String refreshToken = "yyyyy";
	//private String client_id    = "1011307938665-24gh1m0c5lmdpgiojq0i3vvfkdut815f.apps.googleusercontent.com";
	private String client_id    = "1011307938665-24gh1m0c5lmdpgiojq0i3vvfkdut815f";
	private String redirect_uri = "http://localhost:8088/gapps/ggl.jsp";
	private String scope        = "https://www.googleapis.com/auth/calendar";
	private String client_secret= "dFTP44-uSvQNirQ87qAzIOUK";
	
	public String authenticate() throws ServletException, IOException, ServiceException, GeneralSecurityException{
		HttpTransport transport         = new NetHttpTransport();
		JsonFactory jsonFactory         = new JacksonFactory();
		List <String> scopes = new LinkedList<String>();
	    scopes.add(scope);
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, client_id, client_secret, scopes).build();
	    
	    TokenResponse response = new TokenResponse();
	    response.setAccessToken(access_token);
	    Credential cred = flow.createAndStoreCredential(response, "tapank156@gmail.com");
	    System.out.println(cred.getRefreshToken());
	    GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
	    
	    url.setRedirectUri(redirect_uri);
	    url.setApprovalPrompt("force");
	    url.setAccessType("offline");
	    String authSubUrl = url.build();
	    System.out.println(authSubUrl);
		// Exception is thrown HERE.
		//CalendarFeed resultFeed = calendarService.getFeed(feedUrl, CalendarFeed.class);		
		
		
		
		return authSubUrl;
	}
}
