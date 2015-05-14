package ru.majestic.bashimreader.utils;

public class QuotesDictionary {
	
	private static String URL_SERVER 							= "http://bash.im/";	
	
	private static String PREFIX_RANDOM_QUOTE_UTF_8 		= "?u";
	
	public static String PREFIX_NEW_QUOTES_PAGE 				= "index/";
	public static String PREFIX_SINGLE_QUOTES_PAGE 			= "quote/";
	
	public static String URL_QUOTES_NEW 						= URL_SERVER;
	public static String URL_QUOTES_RANDOM 					= URL_SERVER + "random";
	public static String URL_QUOTES_BEST 						= URL_SERVER + "best";
	public static String URL_QUOTES_BY_RATING 				= URL_SERVER + "byrating";
	public static String URL_ABYSS 								= URL_SERVER + "abyss";
	public static String URL_ABYSS_TOP 							= URL_SERVER + "abysstop";  
	public static String URL_ABYSS_BEST 						= URL_SERVER + "abyssbest/";
	public static String URL_SINGLE_QUOTE						= URL_SERVER + PREFIX_SINGLE_QUOTES_PAGE;
	
	public static String URL_RANDOM_QUOTE						= URL_SERVER + "forweb/";
	public static String URL_RANDOM_QUOTE_UTF_8				= URL_SERVER + "forweb/" + PREFIX_RANDOM_QUOTE_UTF_8;		

}
