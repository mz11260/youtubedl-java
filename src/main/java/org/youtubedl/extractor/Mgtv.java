package org.youtubedl.extractor;


import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;

/**
 * MGTV 视频地址解析
 *
 * http://guid.mgtv.com/pc/distribute.do?callback=STK.$.getGUID&%5Bobject%20Object%5D=1506327096928
 *
 */
public class Mgtv extends Common implements URLParser {

	private static final String apiUrl = "http://pcweb.api.mgtv.com/player/video?video_id=%s&cid=%s&keepPlay=0&vid=&watchTime=0";
	private static final String GUID_URL = "http://guid.mgtv.com/pc/distribute.do";
	private static final String LOGIN_USER = "http://u.api.mgtv.com/user/get_login_user?_=%s";

	private String guid;
	private String __guid;
	private String stkuuid;
	private long actionTime;
	private String sessionid;
	private Map<String, String> cookies;


	@Override
	public ResultData parse(String url) {
		final ResultData resultData = new ResultData();

		try {
			// 请求
			__guid = getGUid(GUID_URL);
			this.stkuuid = UUID.randomUUID().toString();
			if (cookies == null) {
				cookies = new HashMap<String, String>();
			}
			sessionid = String.valueOf(System.currentTimeMillis());
			cookies.put("__STKUUID", stkuuid);
			cookies.put("sessionid", sessionid);
			cookies.put("lastActionTime", String.valueOf(System.currentTimeMillis()));
			// 请求sessionid
			sendRequest(String.format(LOGIN_USER, String.valueOf(System.currentTimeMillis())));

			String vid = matchVid(url);
			String cid = matchCid(url);
			guid = getGUid(GUID_URL);
			actionTime = System.currentTimeMillis();

			// 拼装请求地址
			String api = String.format(apiUrl, vid, cid);

			initCookies();

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

					String streamURL = obj.get("url").getAsString();
					if (streamURL == null || "".equals(streamURL)) {
						continue;
					}
					String chk = UUID.randomUUID().toString().replace("-", "");
					streamURL += "&ver=0.2.12943&chk=" + chk + "&ld" + sessionid;
					String urlData = getResponseJson(domain + streamURL);
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

	private void sendRequest(String url) throws IOException {
		try {
			Jsoup.connect(url)
					.headers(setHeaders())
					.cookies(this.cookies)
					.timeout(TIMEOUT).ignoreContentType(true).execute();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 请求JSON
	 * @param url
	 * @return
	 */
	@Override
	public String getResponseJson(String url) throws IOException {
		try {
			Connection.Response response = Jsoup.connect(url)
					.headers(setHeaders())
					.cookies(cookies)
					.timeout(TIMEOUT).ignoreContentType(true).execute();
			return response.body();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * cookies
	 * __STKUUID=0df95871-eecf-4011-9441-45c5493c4089;
	 * MQGUID=912254546565664768;
	 * Hm_lvt_7ed5b39fd087844c0268537a47e35211=1506333377;
	 * Hm_lpvt_7ed5b39fd087844c0268537a47e35211=1506333377;
	 * __MQGUID=912254546477584384;
	 * lastActionTime=1506333377729
	 *
	 */
	private void initCookies() {
		if (cookies == null) {
			this.cookies = new HashMap<String, String>();
		}
		cookies.put("MQGUID", guid);
		cookies.put("Hm_lvt_7ed5b39fd087844c0268537a47e35211", String.valueOf(actionTime/1000));
		cookies.put("Hm_lpvt_7ed5b39fd087844c0268537a47e35211", String.valueOf(actionTime/1000));
		cookies.put("__MQGUID", __guid);
		cookies.put("lastActionTime", String.valueOf(actionTime));
	}

	
	/**
     * 从源URL中获取 vid
     * @param videoUrl
     * @return
     */
    private String matchVid(String videoUrl) {
        return videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.lastIndexOf("."));
    }

    private String matchCid (String videoUrl) {
		Matcher matcher = Pattern.compile("b/(.*?)/").matcher(videoUrl);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
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

	private String getGUid(String url) {
		try {
			Connection.Response response = super.getResponse(url);
			Map<String, String> cookies = response.cookies();
			if (cookies != null && cookies.containsKey("MQGUID")) {
				return cookies.get("MQGUID");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
