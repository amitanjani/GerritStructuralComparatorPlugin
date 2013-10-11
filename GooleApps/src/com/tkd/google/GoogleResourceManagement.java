package com.tkd.google;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.security.sasl.AuthenticationException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.ServiceException;
//import com.google.api.client.util.GenericData;
//import com.google.gdata.client.Service;
//import com.google.gdata.client.appsforyourdomain.AppsForYourDomainService;
//import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
//import com.google.gdata.client.calendar.CalendarService;
//import com.google.gdata.data.analytics.ManagementEntry;
//import com.google.gdata.data.calendar.ResourceProperty;
//import com.google.gdata.model.MetadataRegistry;

public class GoogleResourceManagement {
	private static final Map<String, Account> accounts = new HashMap<String, Account>();
	static{
		accounts.put("imaginea", new Account("imaginea", "siteadmin@imaginea.com", "pramati123", "imaginea.com", "Meeting Room Booking"));
		accounts.put("pramati", new Account("pramati", "jitendra.b@pramati.com", "pramati123", "pramati.com", "Meeting Room Booking"));
		accounts.put("qontext", new Account("qontext", "sysadmin@qontext.com", "pramati@123", "qontext.com", "Meeting Room Booking"));
		accounts.put("kumar", new Account("kumar", "amit.anjani@kumarstest.mygbiz.com", "utkarsha", "kumarstest.mygbiz.com", "Meeting Room Booking"));
	}
	
	//private String serviceName;
	private Account account = null;
	private AppsPropertyService service=null;
	private String feedUrl = null; 
	public static Map<String, Account> getAccounts() {
		return accounts;
	}

	GoogleResourceManagement(String serviceName) throws Exception{
//		File file = new File("/home/tapan/logs/res-out.txt");
//		FileOutputStream fos = new FileOutputStream(file);
//		PrintStream ps = new PrintStream(fos);
//		System.setOut(ps);
		//this.serviceName = serviceName;
		this.account = accounts.get(serviceName);
		//this.service = getService(false);
		this.service = getService(true);
		this.feedUrl = "https://apps-apis.google.com/a/feeds/calendar/resource/2.0/" + this.account.getDomain();
		//this.feedUrl = "https://apps-apis.google.com/a/feeds/calendar/resource/2.0/" + this.account.getDomain() + "/?xoauth_requestor_id=" + account.getEmail();
		
		System.out.println(feedUrl);
		
	}
	
	public void execute() throws Exception{
		try {
			URL urlFeed = new URL(feedUrl);
			 Map<String, Resource> resources = account.getResources();
			 Set<Entry<String, Resource>> entrySet = resources.entrySet();
			 String domain = account.getDomain();
			 for (Entry<String, Resource> entry : entrySet){
				 Resource resource = entry.getValue();
				 resource.setEmail(domain);
				 GenericEntry resourceEntry = new GenericEntry();
				 resourceEntry.declareExtensions(service.getExtensionProfile());
				 resourceEntry.addProperty("resourceId", resource.getId() );
				 resourceEntry.addProperty("resourceCommonName", resource.getName());
				 resourceEntry.addProperty("resourceDescription", resource.getDescription());
				 //resourceEntry.addProperty("resourceEmail", resource.getEmail());
				 resourceEntry.addProperty("resourceType", resource.getType());
				 System.out.println(resource);
				 System.out.println(urlFeed.toString());
				 service.insert(urlFeed, resourceEntry);			
				// GenericFeed gEntry = service.getFeed(new URL(feedUrl + "/" + resource.getId()), GenericFeed.class);
				 //GenericEntry gEntry = service.getEntry(new URL(feedUrl + "/" + resource.getId()) , GenericEntry.class);
//				 updateResource(gEntry,resource.getName());
				 //System.out.println(resource.toString());
				 //System.out.println(resource.getName() + " # " + " $ " + gEntry.getId() + " $ " + gEntry.getExtensionLocalName());
				 //resourceEntry = null;
		     }
			 feedUrl = null;
			 service = null;
		 } catch (MalformedURLException ex) {
			 ex.printStackTrace();
		 } catch(AuthenticationException ex) {
			 ex.printStackTrace();
		 } catch (AppsForYourDomainException ex) {
			 ex.printStackTrace();
		 } catch (IOException ex) {
			 ex.printStackTrace();
		 } catch (ServiceException ex) {
			 ex.printStackTrace();
		 }
	}
	
	AppsPropertyService getService(boolean isjson) throws Exception{ 
		AppsPropertyService service = new AppsPropertyService(account.getApplName());
		if(isjson){
			service.setOAuth2Credentials(getCredential());
		}else{
			System.out.println(account.toString());
			service.setUserCredentials(account.getEmail(), account.getPassword(),ClientLoginAccountType.HOSTED);
			//service.setUserCredentials(account.getEmail(), account.getPassword());
		}				
		return service;
	}
	
	private List<GenericEntry> retrieveAllPages(URL feedUrl) throws IOException, ServiceException {
		List<GenericEntry> allEntries = new ArrayList<GenericEntry>();
		try {
			do {
				GenericFeed feed = service.getFeed(feedUrl, GenericFeed.class);
		        allEntries.addAll(feed.getEntries());
		        feedUrl = (feed.getNextLink() == null) ? null : new URL(feed.getNextLink().getHref());
			} while (feedUrl != null);
		} catch (ServiceException se) {
			AppsForYourDomainException ae = AppsForYourDomainException.narrow(se);
			throw (ae != null) ? ae : se;
		}
		return allEntries;
	}	
	
	public GenericEntry updateResource(GenericEntry entry, String newName)throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
		String oldName = entry.getProperty("resourceCommonName");
		entry.addProperty("resourceCommonName", oldName);
		entry.addProperty("resourceCommonName", newName);
		return service.update(new URL(feedUrl + "/" + entry.getProperty("resourceId")), entry);
	}	
	
	GoogleService getService() throws Exception{ 
		GoogleService service = new GoogleService(account.getEmail(), account.getApplName());
		service.setOAuth2Credentials(getCredential());
		return service;
	}

	Credential getCredential() throws Exception{
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, GoogleResourceManagement.class.getResourceAsStream(account.getName()+".client_secrets.json"));		  
      	if (clientSecrets.getDetails().getClientId().startsWith("Enter")
    		  || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
    	  throw new Exception("Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
    			  + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
      	}
      
      	FileCredentialStore credentialStore = new FileCredentialStore(new File(System.getProperty("user.home"), ".credentials/"+ account.getName()+".calendar.json"), jsonFactory);
            	
        String scope = "https://apps-apis.google.com/a/feeds/calendar/resource/";
      	//String scope = "https://www.googleapis.com/auth/";
       // String scope = "https://www.googleapis.com/auth/calendar";
        //https://apps-apis.google.com/a/feeds/calendar/resource/#readonly
        //Arrays.asList(scopes)
        HashSet<String> scopes = new HashSet<String>();
        //scopes.add("https://apps-apis.google.com/a/feeds/calendar/resource/");
        //scopes.add("https://apps-apis.google.com/a/feeds/calendar");
      	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
    		  httpTransport, jsonFactory, clientSecrets, Arrays.asList(scope)).setCredentialStore(credentialStore).build();

	   Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	   GoogleCredential googleCredential = new GoogleCredential().setAccessToken(credential.getAccessToken());
	   System.out.println(credential.getAccessToken());
	   
	   return credential;
	}
}
