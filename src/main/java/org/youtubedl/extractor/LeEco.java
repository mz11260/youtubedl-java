package org.youtubedl.extractor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;

import java.util.*;

public class LeEco extends Common implements URLParser {

	private static String API_URL = "http://player-pc.le.com/mms/out/video/playJson?id=%s&platid=1&splatid=101&format=1&source=1000&tkey=%s&domain=www.le.com&region=cn&accesyx=1";
	private static String PARAMS = "&m3v=1&format=1&expect=3&tss=ios";

	@Override
	public ResultData parse(String url) {
		ResultData resultData = new ResultData();
		String vid = matchVid(url);
		try {
			String apiUrl = String.format(API_URL, vid, String.valueOf(calcTimeKey(new Date().getTime() / 1000)));
			String json = getResponseJson(apiUrl);

			JsonObject msgs = this.parserJsonObject(json).getAsJsonObject("msgs");
			JsonObject playurl = msgs.getAsJsonObject("playurl");

			if (playurl != null && !playurl.isJsonNull()) {
				List<Video> videos = new ArrayList<Video>();

				String title = playurl.get("title").getAsString();
				int duration = playurl.get("duration").getAsInt() * 1000;
				String thumb = playurl.get("pic").getAsString();

				JsonArray domain = playurl.getAsJsonArray("domain");
				JsonObject dispatch = playurl.getAsJsonObject("dispatch");
				Iterator iterator = dispatch.entrySet().iterator();
				int size = dispatch.entrySet().size();
				while (iterator.hasNext()) {
					Map.Entry map = (Map.Entry) iterator.next();
					String key = (String) map.getKey();
					JsonArray array = (JsonArray) map.getValue();

					Video video = new Video();
					video.setSrcUrl(url);
					video.setTitle(title);
					video.setLogo(thumb);
					video.setVideoMilliseconds(duration);
					video.setStreamType(key);
					video.setHdtv("h" + size);
					String url1 = domain.get(0).getAsString() + array.get(0).getAsString() + PARAMS;
					JsonObject playUrlJson = this.parserJsonObject(getResponseJson(url1));
					JsonArray nodelist = playUrlJson.getAsJsonArray("nodelist");
					video.setPlayUrl(nodelist.get(0).getAsJsonObject().get("location").getAsString());
					videos.add(video);
					size--;
				}
				resultData.setVideos(videos);
				return resultData;
			} else {
				resultData.setError(new Error(msgs.get("statuscode").getAsInt(),
						String.format("获取失败：%s", msgs.getAsJsonObject("playstatus"))));
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
	 * http://www.le.com/ptv/vplay/30805025.html
	 * 从源URL中获取 vid
	 * @param videoUrl
	 * @return
	 */
	private String matchVid(String videoUrl) {
		return videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.lastIndexOf("."));
	}

	private static long calcTimeKey(long param1) {
		int loc2 = 185025305;
		return ror(param1, loc2 % 17) ^ loc2;
	}

	private static long ror(long param1, long param2) {
		long loc3 = 0;
		while (loc3 < param2) {
			param1 = urshift(param1, 1) + ((param1 & 1) << 31);
			loc3 += 1;
		}
		return param1;
	}

	private static long urshift(long val, long n){
		return val >= 0 ? val >> n : (val + 0x100000000L) >> n;
	}

}
