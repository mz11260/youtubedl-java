package org.youtubedl.extractor;

import java.io.IOException;
import java.util.*;

import org.omg.CORBA.TIMEOUT;
import org.youtubedl.pojo.Video;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.nodes.Document;

public class Common {

	protected final static int TIMEOUT = 30000;


	/**
	 * 设置请求 header
	 * @return
	 */
	protected Map<String, String> setHeaders() {
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    	headers.put("Accept-Encoding", "gzip, deflate");
    	headers.put("Accept-charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
    	headers.put("Accept-Language", "en-us,en;q=0.5");
    	headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
    	return headers;
    }
	
	/**
     * 请求Response
     * @param url
     * @return
     */
	protected Response getResponse(String url) throws IOException {
        try {
        	return Jsoup.connect(url)
        			.headers(setHeaders())
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
	protected String getResponseJson(String url) throws IOException {
        try {
        	Response response = Jsoup.connect(url)
        			.headers(setHeaders())
                    .timeout(TIMEOUT).ignoreContentType(true).execute();
            return response.body();
        } catch (IOException e) {
            throw e;
        }
    }
	
	
	/**
	 * 解析json字符串
	 * @param jsonstr
	 * @return
	 */
	protected JsonObject parserJsonObject(String jsonstr) {
		return new JsonParser().parse(jsonstr).getAsJsonObject();
	}

	/**
	 * 加载html页面
	 * @param url
	 * @return
	 * @throws IOException
	 */
	protected Document getHtmlDocument(String url) throws IOException {
		return Jsoup.connect(url)
				.headers(setHeaders())
				.timeout(TIMEOUT).get();
	}

	/**
	 * 按清晰度由低到高排序
	 * @param list
	 */
	protected void sort(List<Video> list) {
		Collections.sort(list,new Comparator<Video>(){
			public int compare(Video arg0, Video arg1) {
				return arg1.getHdtv().compareTo(arg0.getHdtv());
			}
		});
	}
}
