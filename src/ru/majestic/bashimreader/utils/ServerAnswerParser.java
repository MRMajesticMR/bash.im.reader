package ru.majestic.bashimreader.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.majestic.bashimreader.data.Quote;

public class ServerAnswerParser {

	public static final int RATING_QUESTIONS = -99999;
	public static final int RATING_DOTS = -99999;

	public static Quote parseSingleQuotePage(String pageContent) {
		Document doc = Jsoup.parse(pageContent);

		Elements texts = doc.getElementsByClass("text");
		Elements ids = doc.getElementsByClass("id");
		Elements ratings = doc.getElementsByClass("rating");
		Elements dates = doc.getElementsByClass("date");

		Quote quote;
		if (ratings.get(0).html().equals("???"))
			quote = new Quote(Integer.valueOf(ids.get(0).html().replace("#", "")), RATING_QUESTIONS, texts.get(0).html(), dates.get(0).html());

		else if (ratings.get(0).html().equals("..."))
			quote = new Quote(Integer.valueOf(ids.get(0).html().replace("#", "")), RATING_DOTS, texts.get(0).html(), dates.get(0).html());

		else
			quote = new Quote(Integer.valueOf(ids.get(0).html().replace("#", "")), Integer.valueOf(ratings.get(0).html()),
					texts.get(0).html(), dates.get(0).html());

		return quote;
	}
	
	public static String getRandomQuoteURLFromServerAnswer(String serverAnswer) {		
		String url = serverAnswer.substring(serverAnswer.indexOf("a href=\"http://bash.im/quote/") + "a href=\"".length());
		url = url.substring(0, url.indexOf("\">#"));
		return url;
	}

}
