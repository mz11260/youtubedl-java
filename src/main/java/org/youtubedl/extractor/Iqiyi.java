package org.youtubedl.extractor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;
import org.youtubedl.util.Md5Util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 爱奇艺视频地址解析
 *
 */
public class Iqiyi extends Common implements URLParser {

	/**
	 * MD5 加密密钥
	 * 请求中的sc参数md5计算规则 time + key + tvid
	 */
	private static final String MD5_KEY = "d5fb4bd9d50c4be6948c97edd7254b0e";
	private static final String SRC = "76f90cbd92f94a2e925d83e8ccd22cb7";

	/**
	 * 视频资源请求地址
	 */
	private static final String API_URL = "http://cache.m.iqiyi.com/jp/tmts/%s/%s/?tvid=%s&vid=%s&src=%s&sc=%s&t=%s";

	@Override
	public ResultData parse(String url) {
		final ResultData resultData = new ResultData();

		try {
			String html = getHtmlDocument(url).outerHtml();
			String tvid = getTvid(html);
			String videoId = getVideoId(html);
			if (tvid == null || "".equals(tvid)) {
				resultData.setError(new Error(500, "Can't find any video"));
				resultData.setVideos(null);
				return resultData;
			}

			String responseJson = getRawData(tvid, videoId);
			JsonObject json = this.parserJsonObject(responseJson);

			if (!"A00000".equals(json.get("code").getAsString())) {
				if ("A00111".equals(json.get("code").getAsString())) {
					resultData.setError(new Error(500, "您所在的位置无法播放该视频"));
				} else {
					resultData.setError(new Error(500, "无法加载数据"));
				}
				return resultData;
			}

			JsonObject data = json.getAsJsonObject("data");
			int duration = data.get("duration").getAsInt();
			JsonArray vidl = data.getAsJsonArray("vidl");

			List<Video> videos = new ArrayList<Video>();
			for (JsonElement element : vidl) {
				Video video = new Video();
				video.setSrcUrl(url);
				video.setTitle("");
				video.setLogo("");
				JsonObject obj = element.getAsJsonObject();
				video.setPlayUrl(obj.get("m3utx").getAsString());
				video.setSize(0);
				video.setStreamType(obj.get("vd").getAsString());
				video.setVideoMilliseconds(duration * 1000);
				video.setHdtv(FORMAT_MAP.get(video.getStreamType()));
				videos.add(video);
			}
			sort(videos);
			resultData.setVideos(videos);
			return resultData;
		} catch (Exception e) {
			resultData.setError(new Error(500, e.getMessage()));
			resultData.setVideos(null);
			return resultData;
		}
	}


	/**
	 * 从网页中获取 tvid
	 * @param html
	 * @return
	 */
	private String getTvid (String html) {
		Matcher matcher = Pattern.compile("data-(?:player|shareplattrigger)-tvid\\s*=\\s*[\\'\"](\\d+)").matcher(html);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * 从网页中获取 video_id
	 * @param html
	 * @return
	 */
	private String getVideoId (String html) {
		Matcher matcher = Pattern.compile("data-(?:player|shareplattrigger)-videoid\\s*=\\s*[\\'\"]([a-f\\d]+)").matcher(html);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * 请求json
	 * @param tvid
	 * @param videoId
	 * @return
	 * @throws IOException
	 */
	private String getRawData (String tvid, String videoId) throws IOException {
		String time = String.valueOf(new Date().getTime());
		String sc = Md5Util.getMd5(time + MD5_KEY + tvid);
		String requestUrl = String.format(API_URL, new String[]{tvid, videoId, tvid, videoId, SRC, sc, time});
		String json = getResponseJson(requestUrl);
		return json.replace("var tvInfoJs=", "");
	}

	private static Map<String, String> FORMAT_MAP = new HashMap<String, String>();
	static {
		FORMAT_MAP.put("18", "h1"); // 1080p
		FORMAT_MAP.put("5", "h2");  // 1072p, 1080p
		FORMAT_MAP.put("4", "h3");  // 720p
		FORMAT_MAP.put("17", "h3"); // 720p
		FORMAT_MAP.put("21", "h4"); // 504p
		FORMAT_MAP.put("2", "h5");  // 480p, 504p
		FORMAT_MAP.put("1", "h6");  // 336p, 360p
		FORMAT_MAP.put("96", "h7"); // 216p, 240p
	}

}
