import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.youtube.model.SearchResult;

public class YoutubeSearchResponse {

	private List<SearchResult> searchResultList;
	
	private List<YoutubeSearchResult> result;
	
	private Map<String, YoutubeSearchResult> asMap;
	
	public YoutubeSearchResponse() {
		asMap = new HashMap<>();
		result = new ArrayList<>();
	}
	
	public void setSearchResult(List<SearchResult> searchResultList) {
		this.searchResultList = searchResultList;
		for ( SearchResult r: searchResultList) {
			YoutubeSearchResult video = new YoutubeSearchResult();
			video.setVideoId(r.getId().getVideoId());
			video.setTitle(r.getSnippet().getTitle());
			asMap.put(r.getId().getVideoId(), video);
			result.add(video);
//			entry.addThumbnail(thumbnail.getUrl().replaceAll("default", "mqdefault"), entry.getLanguageList().iterator().next().toLowerCase());
		}
	}

	public String getVideoIdList() {
		return asMap.keySet().toString();
	}
	
	public Map<String, YoutubeSearchResult> getAsMap() {
		return asMap;
	}

	public List<YoutubeSearchResult> getResult() {
		return result;
	}

	public void setResult(List<YoutubeSearchResult> result) {
		this.result = result;
	}

}
