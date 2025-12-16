package com.insightcrew.domain.trip.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripApiResponse {

	private Response response;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Response {
		private Header header;
		private Body body;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Header {
		private String resultCode;
		private String resultMsg;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body {
		private Object items;
		private int numOfRows;
		private int pageNo;
		private int totalCount;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Items {
		private List<Item> item;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private String title;
		private String addr1;
		private String addr2;
		private String firstimage;
		private String firstimage2;
		private String mapx;
		private String mapy;
		private String contentid;
		private String contenttypeid;
		private String areacode;
		private String sigungucode;
		private String cat1;
		private String cat2;
		private String cat3;
		private String zipcode;
		private String tel;
		private String homepage;
		private String overview;
	}
}
