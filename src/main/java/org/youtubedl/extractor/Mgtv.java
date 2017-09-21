package org.youtubedl.extractor;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;

/**
 * MGTV 视频地址解析
 *
 */
public class Mgtv extends Common implements URLParser {

	private static final String apiUrl = "http://pcweb.api.mgtv.com/player/video?video_id=%s";

	@Override
	public ResultData parse(String url) {
		final ResultData resultData = new ResultData();

		String vid = matchVid(url);

		// 拼装请求地址
		String api = String.format(apiUrl, vid);
		try {
			String responseJson = getResponseJson(api);
			JsonObject json = parserJsonObject(responseJson);
			JsonObject data = json.getAsJsonObject("data");

			if (data != null && !data.isJsonNull()) {
				List<Video> videos = new ArrayList<Video>();
				String domain = data.getAsJsonArray("stream_domain").get(0).getAsString();
				JsonObject info = data.getAsJsonObject("info");
				String title = info.get("title").getAsString();
				String thumb = info.get("thumb").getAsString();
				int duration = info.get("duration").getAsInt() * 1000;

				JsonArray stream = data.getAsJsonArray("stream");
				for (JsonElement element : stream) {
					Video video = new Video();
					video.setSrcUrl(url);
					video.setTitle(title);
					video.setLogo(thumb);
					video.setVideoMilliseconds(duration);

					JsonObject obj = element.getAsJsonObject();
					video.setStreamType(obj.get("name").getAsString());
					video.setHdtv(setHdtv(video.getStreamType()));
					String urlData = getResponseJson(domain + obj.get("url").getAsString());
					JsonObject urlJson = parserJsonObject(urlData);
					video.setPlayUrl(urlJson.get("info").getAsString());
					videos.add(video);
				}
				sort(videos);
				resultData.setVideos(videos);
				return resultData;
			} else {
				resultData.setError(new Error(json.get("code").getAsInt(), json.get("msg").getAsString()));
				resultData.setVideos(null);
				return resultData;
			}
		} catch (Exception e) {
			resultData.setError(new Error(500, e.getMessage()));
			resultData.setVideos(null);
			return resultData;
		}
	}

	
	/**
     * 从源URL中获取 vid
     * @param videoUrl
     * @return
     */
    private String matchVid(String videoUrl) {
        return videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.lastIndexOf("."));
    }
    
    private String setHdtv(String streamType) {
    	if ("标清".equals(streamType)) {
    		return "h3";
    	} else if ("高清".equals(streamType)) {
    		return "h2";
    	} else if ("超清".equals(streamType)) {
    		return "h1";
    	} else {
    		return "h3";
    	}
    }

}
