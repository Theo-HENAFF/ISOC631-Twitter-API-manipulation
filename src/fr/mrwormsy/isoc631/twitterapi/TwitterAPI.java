package fr.mrwormsy.isoc631.twitterapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI {

	private static Twitter twitter;
	
	public static void main(String[] args) {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		  .setOAuthConsumerKey("dspktk1iXqA2yYHBrVkncPQw9")
		  .setOAuthConsumerSecret("F323o3PI0UrO9ZvB4WWKx21QV3zxp1RrRF4aY4Rj1DR3beJCeL")
		  .setOAuthAccessToken("1090251522473160705-sA7oi1AIiNTNJUl2A2PjGngijy44vy")
		  .setOAuthAccessTokenSecret("qzIJ66jtiyxKHB0zwBhiifKSL4c9RLj2LGaHevnr92BuV");
		//TwitterFactory tf = new TwitterFactory(cb.build());
		//twitter = tf.getInstance();
		
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
		StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);


        FilterQuery filtre = new FilterQuery();
        String[] keywordsArray = {"Big Data"};
        filtre.track(keywordsArray);
        twitterStream.filter(filtre);
		
        /*
        
		try {

			//List<String> string = 
			
			//searchtweets("Trump");
			
			//System.out.println(string.size());
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		
		
	}
	
	public static String createTweet(String tweet) throws TwitterException {
	    Status status = twitter.updateStatus(tweet);
	    return status.getText();
	}

	public static List<String> getTimeLine() throws TwitterException {
	    return twitter.getHomeTimeline().stream().map(item -> item.getText()).collect(Collectors.toList());
	}
	
	public static String sendDirectMessage(String recipientName, String msg) throws TwitterException {
		DirectMessage message = twitter.sendDirectMessage(recipientName, msg);
		return message.getText();
	}
	
	public static ArrayList<Status> searchtweets(String search) throws TwitterException {
		  Query query = new Query(search);
		  int numberOfTweets = 512;
		  long lastID = Long.MAX_VALUE;
		  ArrayList<Status> tweets = new ArrayList<Status>();
		  while (tweets.size () < numberOfTweets) {
		    if (numberOfTweets - tweets.size() > 100)
		      query.setCount(100);
		    else 
		      query.setCount(numberOfTweets - tweets.size());
		    try {
		      QueryResult result = twitter.search(query);
		      tweets.addAll(result.getTweets());
		      
		      System.out.println("Gathered " + tweets.size() + " tweets");
		      for (Status t: tweets) 
		        if(t.getId() < lastID) lastID = t.getId();

		    }

		    catch (TwitterException te) {
		    	System.out.println("Couldn't connect: " + te);
		    }; 
		    query.setMaxId(lastID-1);
		  }

		  for (int i = 0; i < tweets.size(); i++) {
		    Status t = (Status) tweets.get(i);

		    GeoLocation loc = t.getGeoLocation();

		    String user = t.getUser().getScreenName();
		    String msg = t.getText();
		    String time = "";
		    if (loc!=null) {
		      Double lat = t.getGeoLocation().getLatitude();
		      Double lon = t.getGeoLocation().getLongitude();
		      System.out.println(i + " USER: " + user + " wrote: " + msg + " located at " + lat + ", " + lon);
		    } 
		    else 
		    	System.out.println(i + " USER: " + user + " wrote: " + msg);
		  }
		  
		  return tweets;
	}
	
	/*
	
	public static List<String> searchtweets(String search) throws TwitterException {
	    Query query = new Query(search);
	    QueryResult result = twitter.search(query);
	     
	    return result.getTweets().stream().map(item -> item.getText()).collect(Collectors.toList());
	}
	
	*/
	
}
