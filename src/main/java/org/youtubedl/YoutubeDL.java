package org.youtubedl;


import org.youtubedl.extractor.*;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;
import org.youtubedl.util.GsonUtil;
import org.youtubedl.util.UrlUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 视频真实播放地址解析
 *
 */
public class YoutubeDL {
	
	private final static String LETV = "le.com";
	private final static String YOUKU = "youku.com";
	private final static String IQIYI = "iqiyi.com";
	private final static String MGTV = "mgtv.com";
	private final static String SOHU = "sohu.com";
	
	public static ResultData parser(String url) {
		
		if (url == null || "".equals(url)) {
			ResultData resultData = new ResultData();
			resultData.setError(new Error(404, "源地址不能为空"));
			resultData.setVideos(null);
			return resultData;
		}
		
		URLParser parser = null;
		url = url.trim();
		String domain = UrlUtil.getDomain(url);
		if (domain.contains(YOUKU)) {
			parser = new Youku();
		} else if (domain.contains(LETV)) {
			parser = new LeEco();
		} else if (domain.contains(IQIYI)) {
			parser = new Iqiyi();
		} else if (domain.contains(MGTV)) {
			parser = new Mgtv();
		} else if (domain.contains(SOHU)) {
			parser = new Sohu();
		} else {
			ResultData resultData = new ResultData();
			resultData.setError(new Error(500, "不支持的URL"));
			return resultData;
		}
		return parser.parse(url);
	}

	/**
	 * @param url
	 * @return
	 */
	public static String parserJson(String url) {
		ResultData resultData = parser(url);
		if (resultData.getVideos() != null) {
			return GsonUtil.toJson(resultData.getVideos());
		} else {
			return null;
		}
	}

	/**
	 * @param url
	 * @return
	 */
	public static List<String> parserList(String url) {
		ResultData resultData = parser(url);
		if (resultData.getVideos() != null) {
			List<Video> list = resultData.getVideos();
			List<String> playUrls = new ArrayList<String>();
			for (Video video : list) {
				playUrls.add(video.getPlayUrl());
			}
			return playUrls;
		} else {
			return null;
		}
	}
	
}
