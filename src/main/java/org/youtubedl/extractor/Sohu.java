package org.youtubedl.extractor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.youtubedl.URLParser;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/9/15.
 *
 * 移动端获取m3u8
 * http://m.tv.sohu.com/phone_playinfo?vid=2892496&site=1&appid=tv&api_key=f351515304020cad28c92f70f002261c&plat=17&
 * sver=1.0&partner=1&uid=1505873845992691&muid=1505873845991996&_c=1&pt=3&qd=680&src=11060001&_=1505873846148
 *
 * API_PARAMS: {api_key: n.API_KEY, plat: "17", sver: "1.0", partner: "1"}
 *
 * Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1
 *
 * PC端获取剧集
 * http://pl.hd.sohu.com/videolist?callback=jQuery17201260217615491075_1505729386489&playlistid=5102665&pagenum=8&pagesize=100&order=0
 * &preVideoRule=1&_=1505729394506
 *
 * 移动端获取剧集
 * http://api.tv.sohu.com/v4/album/videos/5102665.json?callback=jsonpx1505721636182_40_13&page=27&site=1&with_trailer=1
 * &with_fee_video=3&with_prevideo=1&prevideo_rule=1&api_key=695fe827ffeb7d74260a813025970bd5&page_size=30&order=0&_=1505721636182
 *
 * 这两个连接干嘛的？ 被限速是否与此有关
 * http://score.my.tv.sohu.com/digg/get.do?vid=2892496&type=16&tvid=82975713&_=1505873846105
 *
 * http://m.aty.sohu.com/h?pt=15577|15067&poscode=15577|15067&plat=h3&sysver=9.1&c=tv&cat=1&vc=115103&pn=iphone&al=9103260&ag=&st=&site=1&ar=6&vu=&
 * tuv=1505873845992691&appid=tv&type=vrs&vid=2892496&lid=&tvid=82975713&pageUrl=http://m.tv.sohu.com/20160218/n437663719.shtml&du=394.722&partner=11060001&
 * playstyle=&useragent=Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1&_=1505873846135
 *
 * http://pv.sohu.com/suv/?t?=1505900053040386_414_736?r?=http://tv.sohu.com/20170622/n600017924.shtml
 * http://m.tv.sohu.com/h5/coopauth/1106.json?callback=jsonpx1505899308171_16_11&platform=3&vid=3825783&pid=5102665&play=832&site=1&cid=16&video_type=1&isSohu=0&_=1505899308171
 */
public class Sohu extends Common implements URLParser {

    private static final String API_URL = "http://m.tv.sohu.com/phone_playinfo?vid=%s&site=1&appid=tv&api_key=f351515304020cad28c92f70f002261c" +
            "&plat=17&sver=1.0&partner=1&uid=%s&muid=%s&_c=1&pt=3&qd=680&src=11060001&_=%s";

    private static final String SCORE_URL = "http://score.my.tv.sohu.com/digg/get.do?vid=%s&type=%s&tvid=%s&_=%s";
    private static final String ATY_URL = "http://m.aty.sohu.com/h?pt=15577|15067&poscode=15577|15067&plat=h3&sysver=9.1&c=tv&cat=1&vc=%s&pn=iphone&al=%s&ag=&st=&site=1&" +
            "ar=6&vu=&tuv=%s&appid=tv&type=vrs&vid=%s&lid=&tvid=%s&pageUrl=%s&du=%s&partner=11060001&playstyle=&useragent=%s&_=%s";

    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    private String time;
    private String uid;
    private String muid;
    private String srcURL;

    private Map<String, String> cookies;

    @Override
    public ResultData parse(String url) {
        ResultData resultData = new ResultData();
        url = url.replace("http://tv", "http://m.tv");
        this.srcURL = url;
        try {
            String html = this.getHtmlDocument(url).outerHtml();
            this.uid = getMuid();
            JsonObject videoData = this.parserJsonObject(matchVideoData(html));
            String vid = videoData.get("vid").getAsString();
            this.muid = getMuid();
            initCookies();
            String tvid = videoData.get("tvid").getAsString();
            String cid = videoData.get("cid").getAsString();
            this.getResponseJson(String.format(SCORE_URL, vid, cid, tvid, String.valueOf(System.currentTimeMillis())));
            String cateCode = videoData.get("cateCode").getAsString();
            String playTime = videoData.get("playTime").getAsString();
            String sid = videoData.get("sid").getAsString();
            this.sendRequest(String.format(ATY_URL, cateCode, sid, uid, vid, tvid, url, playTime, USER_AGENT, String.valueOf(System.currentTimeMillis())));

            this.time = String.valueOf(System.currentTimeMillis());
            String apiUrl = String.format(API_URL, vid, uid, muid, time);
            String json = this.getResponseJson(apiUrl);
            JsonObject data = this.parserJsonObject(json).getAsJsonObject("data");
            if (data != null && !data.isJsonNull()) {
                JsonObject urls = data.getAsJsonObject("urls");
                if (urls != null && !urls.isJsonNull()) {
                    JsonObject m3u8 = urls.getAsJsonObject("m3u8");
                    if (m3u8 != null && !m3u8.isJsonNull()) {
                        List<Video> videos = new ArrayList<Video>();
                        Iterator iterator = m3u8.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry map = (Map.Entry) iterator.next();
                            String key = (String) map.getKey();

                            JsonArray array = (JsonArray) map.getValue();
                            if (array.size() == 0) {
                                continue;
                            }
                            Video video = new Video();
                            video.setSrcUrl(url);
                            video.setTitle("");
                            video.setLogo("");
                            video.setVideoMilliseconds(0);
                            video.setStreamType(key);
                            video.setHdtv(setHdtv(key));
                            video.setPlayUrl(array.get(0).getAsString());
                            videos.add(video);
                        }
                        resultData.setVideos(videos);
                        return resultData;
                    }
                }
            }
            resultData.setError(new Error(500, "获取失败"));
            resultData.setVideos(null);
        } catch (Exception e) {
            resultData.setError(new Error(500, e.getMessage()));
            resultData.setVideos(null);
        }
        return resultData;
    }



    public static void main(String[] args) throws IOException {
        Sohu sohu = new Sohu();
            String url = "http://tv.sohu.com/20170622/n600017924.shtml";
        ResultData data = sohu.parse(url);
        Error e = data.getError();
        if (e == null) {
            List<Video> list = data.getVideos();
            for (Video v : list) {
                System.out.println(v.toString());
            }
        } else {
            System.out.println(e.getNote());
            System.out.println(e.getCode());
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
                    .cookies(this.cookies)
                    .timeout(10000).ignoreContentType(true).execute();
            return response.body();
        } catch (IOException e) {
            throw e;
        }
    }

    private void sendRequest(String url) throws IOException {
        try {
            Jsoup.connect(url)
                    .headers(setHeaders())
                    .header("Referer", srcURL)
                    .timeout(10000).ignoreContentType(true).execute();
        } catch (IOException e) {
            throw e;
        }
    }

    private void initCookies() {
        this.cookies = new HashMap<String, String>();
        cookies.put("MTV_SRC", "11060001");
        cookies.put("SUV", uid);
        cookies.put("_muid_", muid);
    }

    /**
     * 加载html页面
     * @param url
     * @return
     * @throws IOException
     */
    @Override
    public Document getHtmlDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .headers(setHeaders())
                .timeout(10000).get();
    }

    @Override
    public Map<String, String> setHeaders() {
        Map<String, String> headers = super.setHeaders();
        headers.put("User-Agent", USER_AGENT);
        return headers;
    }

    /**
     * @param html
     * @return
     */
    private String matchVideoData(String html) {
        // \Wvid\s*[\:=]\s*[\'"]?(\d+)[\'"]?
        Matcher matcher = Pattern.compile("var\\s*videoData\\s*[\\:=]\\s*([\\s\\S]*);[\\s\\S]*var\\s*prevideo?").matcher(html);
        if(matcher.find()) {
            Matcher matcher1 = Pattern.compile("\\\\s*|\\t|\\r|\\n| ").matcher(matcher.group(1));
            return matcher1.replaceAll("").replace(",}", "}");
        }
        return null;
    }

    private String getMuid() {
        return System.currentTimeMillis() + String.format("%03d", new Random().nextInt(1000));
    }

    private String setHdtv(String streamType) {
        if ("sup".equals(streamType)) {
            return "h1";
        } else if ("nor".equals(streamType)) {
            return "h2";
        } else if ("hig".equals(streamType)) {
            return "h3";
        } else {
            return "h1";
        }
    }

}
