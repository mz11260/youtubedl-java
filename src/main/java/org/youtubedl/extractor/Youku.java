package org.youtubedl.extractor;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;
import org.youtubedl.pojo.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;


/**
 * 优酷视频解析
 */
public class Youku extends Common implements URLParser {
	
	private static String playApi = "https://ups.youku.com/ups/get.json?vid=%s&ccode=0402&client_ip=192.168.1.1&utid=%s&client_ts=%s";
	private static String utidURL = "https://log.mmstat.com/eg.js";

	private String referer = "http://v.youku.com";

	private Map<String, String> cookies;

	@Override
	public ResultData parse(String url) {
		initCookies();
		final ResultData resultData = new ResultData();

		this.referer = url;
		String vid = matchVid(url);
		
		try {
			// 拼装请求地址
			String api = getPlayRequestUrl(vid, getCnaUtid());

			try {
				Thread.currentThread().sleep(3000);
			} catch (InterruptedException e) {
				// nothing
			}

			String responseJson = getResponseJson(api);
			JsonObject json = parserJsonObject(responseJson);
			JsonObject data = json.getAsJsonObject("data");
			JsonObject error = data.getAsJsonObject("error");
			if (error != null) {
				resultData.setError(new Error(error.get("code").getAsInt(), error.get("note").getAsString()));
				resultData.setVideos(null);
				return resultData;
			} else {
				
				List<Video> videos = new ArrayList<Video>();
				
				JsonObject videoInfo = data.getAsJsonObject("video");
				String title = videoInfo.get("title").getAsString();
				String logo = videoInfo.get("logo").getAsString();
				JsonArray streams = data.getAsJsonArray("stream");

				for (JsonElement element : streams) {
					Video video = new Video();
					video.setSrcUrl(url);
					video.setTitle(title);
					video.setLogo(logo);
					JsonObject obj = element.getAsJsonObject();
					video.setPlayUrl(obj.get("m3u8_url").getAsString());
					video.setSize(obj.get("size").getAsInt());
					video.setStreamType(obj.get("stream_type").getAsString());
					video.setVideoMilliseconds(obj.get("milliseconds_video").getAsInt());
					video.setHdtv(setHdtv(video.getStreamType()));
					videos.add(video);
				}
				sort(videos);
				resultData.setVideos(videos);
				return resultData;
			}
		} catch (IOException e) {
			resultData.setError(new Error(500, e.getMessage()));
			resultData.setVideos(null);
			return resultData;
		}
	}


    /**
     * 从源URL中获取
     * @param videoUrl
     * @return
     */
    private String matchVid(String videoUrl) {
        Matcher matcher = Pattern.compile("id_(.*?)\\.html").matcher(videoUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    /**
     * @param vid
     * @return
     */
    private String getPlayRequestUrl(String vid, String utid) {
        return String.format(playApi, vid, utid, String.valueOf(new Date().getTime() / 1000));
    }

	/**
	 * @return
	 */
	private String getCnaUtid() throws IOException {
    	try {
			Response response = getResponse(utidURL);
			Map<String, String> cookies = response.cookies();
			if (cookies != null && !cookies.isEmpty()) {
				return cookies.get("cna");
			}
			Map<String, String> headers = response.headers();
			return headers.get("ETag").replace("\"", "");
		} catch (IOException e) {
			throw e;
		}
    }
    
    private static final String SOURCE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static String getYsuid(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(52);// [0,51)
            sb.append(SOURCE.charAt(number));
        }
        return new Date().getTime()/1000 + sb.toString();
    }

	@Override
	protected Response getResponse(String url) throws IOException {
		try {
			return Jsoup.connect(url)
					.headers(setHeaders())
					.cookies(this.cookies)
					.timeout(10000).ignoreContentType(true).execute();
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
        	Response response = Jsoup.connect(url)
        			.cookies(this.cookies)
        			.headers(setHeaders())
        			.header("Referer", this.referer)// Referer header必须设置
                    .timeout(10000).ignoreContentType(false).execute();
            return response.body();
        } catch (IOException e) {
            throw e;
        }
    }
    
    /**
     * 设置请求Cookie
     * @return
     */
    private void initCookies() {
    	this.cookies = new HashMap<String, String>();
    	cookies.put("__ysuid", getYsuid(3));
    	cookies.put("xreferrer", "http://www.youku.com");
    }
    
    private String setHdtv(String streamType) {
    	if ("hd3".equals(streamType)) {
    		return "h1";
    	} else if ("hd2".equals(streamType)) {
    		return "h2";
    	} else if ("mp4hd".equals(streamType)
    			|| "mp4".equals(streamType)) {
    		return "h3";
    	} else if ("mp4hd3".equals(streamType)
    			|| "mp4hd2".equals(streamType)
    			|| "flvhd".equals(streamType)
    			|| "flv".equals(streamType)) {
    		return "h4";
    	} else if ("3gphd".equals(streamType)) {
    		return "h5";
    	} else if ("3gp".equals(streamType)) {
    		return "h6";
    	} else {
    		return "h1";
    	}
    }
    
}
