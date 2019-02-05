package fr.mrwormsy.isoc631.twitterapi;

import java.util.List;
import java.util.stream.Collectors;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI {

	private static Twitter twitter;
	
	public static void main(String[] args) {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("dspktk1iXqA2yYHBrVkncPQw9")
		  .setOAuthConsumerSecret("F323o3PI0UrO9ZvB4WWKx21QV3zxp1RrRF4aY4Rj1DR3beJCeL")
		  .setOAuthAccessToken("1090251522473160705-sA7oi1AIiNTNJUl2A2PjGngijy44vy")
		  .setOAuthAccessTokenSecret("qzIJ66jtiyxKHB0zwBhiifKSL4c9RLj2LGaHevnr92BuV");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		
		try {

			List<String> string = searchtweets("#Concours");
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		System.out.println("------------- DONE -------------");
		
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
	
	public static List<String> searchtweets(String search) throws TwitterException {
	    Query query = new Query(search);
	    QueryResult result = twitter.search(query);
	     
	    return result.getTweets().stream().map(item -> item.getText()).collect(Collectors.toList());
	}	
	
}
