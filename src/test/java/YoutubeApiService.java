import java.io.IOException;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

//https://www.googleapis.com/youtube/v3/videos?part=id,snippet,contentDetails,status&id=BBfPP0rTjxo,o04cSB5afGc&maxResults=50&key=UQMUAvZDVatLDySazIAUemK34MEYvQbSDaZaFPg
//https://www.googleapis.com/youtube/v3/search?q=malayalam&key=UQMUAvZDVatLDySazIAUemK34MEYvQbSDaZaFPg&part=id,snippet

public class YoutubeApiService {

	private YouTube youtube;
	private String KEY = "UQMUAvZDVatLDySazIAUemK34MEYvQbSDaZaFPg";

	public YoutubeApiService() {
		init();
	}

	public void init() {
		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
				request.setConnectTimeout(100);
			    request.setReadTimeout(2000);
			}
		}).setApplicationName("youtube-cmdline-search-sample").build();
	}

	public YoutubeSearchResponse search(SearchCriteria searchCriteria) throws IOException {
		// Define the API request for retrieving search results.

		YouTube.Search.List search = youtube.search().list("id,snippet");
		search.setKey(KEY);
		search.setQ("nadodikkattu");
		// Restrict the search results to only include videos. See:
		// https://developers.google.com/youtube/v3/docs/search/list#type
		search.setType("video");

		// Calendar cal = Calendar.getInstance();
		// cal.setTime(new Date());
		// cal.add(Calendar.DATE, -365);
		// Date dateBefore365Days = cal.getTime();
		// search.setPublishedAfter(new DateTime(dateBefore365Days));

		// To increase efficiency, only retrieve the fields that the
		// application uses.
		search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		search.setMaxResults(20L);

		// Call the API and print results.
		SearchListResponse searchResponse = search.execute();
		YoutubeSearchResponse result = new YoutubeSearchResponse();
		List<SearchResult> searchResultList = searchResponse.getItems();
		System.out.println(searchResponse);
		result.setSearchResult(searchResultList);
		return result;
	}
	
	public void updateDuration(YoutubeSearchResponse youtubeSearchResponse) throws IOException {
		YouTube.Videos.List videos = youtube.videos().list("id,contentDetails");
		videos.setKey(KEY);
		videos.setId(youtubeSearchResponse.getVideoIdList());
		VideoListResponse r = videos.execute();
		for (Video v : r.getItems()) {
			v.getContentDetails().getDuration();
		}
		
	}

	public static void main(String[] args) throws IOException {
		YoutubeApiService service = new YoutubeApiService();
		service.init();
		YoutubeSearchResponse r = service.search(null);
		service.updateDuration(r);
		System.out.println(r.getResult());
	}


}
