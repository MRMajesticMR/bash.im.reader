package ru.majestic.bashimreader.data;

public class Quote {

	private final int id;
	private final int rating;
	private final String content;
	private final String dateString;

	public Quote(final int id, final int rating, final String content, final String dateString) {
		this.id = id;
		this.rating = rating;
		this.content = content;
		this.dateString = dateString;
	}

	public int getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}

	public String getContent() {
		return content;
	}

	public String getDateString() {
		return dateString;
	}

	public String toJSONString() {
		return "{\"id\": " + id + ", \"rating\": " + rating + ", \"content\": \"" + content + "\", \"date\": \"" + dateString + "\"}";
	}

}
