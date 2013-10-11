package com.tkd.google.servlet;

import java.awt.print.Printable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.tkd.google.GoogleAuthUser;
import com.tkd.google.GoogleUtility;

/**
 * Servlet implementation class GoogleServlet
 */
@WebServlet("/GoogleServlet")
public class GoogleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CalendarService calendarService = null;
	String CONSUMER_KEY 	= "imaginea.com";
	String CONSUMER_SECRET 	= "mmca_dvcEjt0VFcXWD4Pg59L";
	String SCOPE        	= "https://www.googleapis.com/auth/calendar";
	String TOKEN 			= "lWxe38u6eTmZmGaQEwpxubQe9k2ZoUQHo4v2ebykiCfsrQ4sYzpPjOxgwEfZ/2ZC";
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String consumerKey = getInitParameter("consumer_key");
        String consumerSecret = getInitParameter("consumer_secret");

        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
        oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);

        calendarService = new CalendarService("marketplace-hello");
        try {
            calendarService.setOAuthCredentials(oauthParameters, new OAuthHmacSha1Signer());
        } catch (OAuthException e) {
            throw new ServletException("Unable to initialize calendar service", e);
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
	}
}
