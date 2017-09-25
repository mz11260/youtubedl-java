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
 * 获取后请求这3个链接
 *
 * http://z.m.tv.sohu.com/pv.gif?url=http://m.tv.sohu.com/20170622/n600017924.shtml&refer=http://tv.sohu.com/20170622/n600017924.shtml
 * &uid=1506047517550801&webtype=&screen=414x736&catecode=115103;115104;115106;115112&pid=5102665
 * &vid=3825783&tvid=90230528&os=ios&platform=iphone&passport=&t=1506047517680&channeled=1211010000&oth=&cd=&MTV_SRC=11060001&sd=
 * cookies
 * SUV=1506047517550801;_muid_=1506047517550938;MTV_SRC=11060001;SOHUSVP=G1LrWmsz7fC2gQv1LItkPewB0JRvDCjvlJS11-5kDTE
 *
 * http://sohu.irs01.com/irt?_iwt_UA=UA-sohu-000001&jsonp=_410RZ
 *
 * http://z.m.tv.sohu.com/h5_cc.gif?t=1506047517736&uid=1506047517550801&position=page_adbanner&op=click&details={}&nid=600017924
 * &url=http://m.tv.sohu.com/20170622/n600017924.shtml&refer=http://tv.sohu.com/20170622/n600017924.shtml&screen=414x736
 * &os=ios&platform=iphone&passport=&vid=3825783&pid=5102665&channeled=1211010000&MTV_SRC=11060001
 *
 */
public class Sohu extends Common implements URLParser {

    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    private String time;
    private String uid;
    private String muid;
    private String srcURL;
    private String screen = "414x736";

    private Map<String, String> cookies;

    private static final String API_URL = "http://m.tv.sohu.com/phone_playinfo?vid=%s&site=1&appid=tv&api_key=f351515304020cad28c92f70f002261c" +
            "&plat=17&sver=1.0&partner=1&uid=%s&muid=%s&_c=1&pt=3&qd=680&src=11060001&_=%s";

    private static final String PV_URL = "http://z.m.tv.sohu.com/pv.gif?url=%s&refer=%s&uid=%s&webtype=&screen=%s&catecode=%s" +
            "&pid=%s&vid=%s&tvid=%s&os=ios&platform=iphone&passport=&t=%s&channeled=%s&oth=&cd=&MTV_SRC=11060001&sd=";

    private static final String IRT_URL = "http://sohu.irs01.com/irt?_iwt_UA=UA-sohu-000001&jsonp=_410RZ";

    private static final String ATY_URL = "http://z.m.tv.sohu.com/h5_cc.gif?t=%s&uid=%s&position=page_adbanner&op=click&details={}&nid=%s" +
            "&url=%s&refer=%s&screen=%s&os=ios&platform=iphone&passport=&vid=%s&pid=%s&channeled=%s&MTV_SRC=11060001";

    @Override
    public ResultData parse(String url) {
        this.srcURL = url;
        ResultData resultData = new ResultData();
        url = url.replace("http://tv", "http://m.tv");
        try {
            String html = this.getHtmlDocument(url).outerHtml();

            if (html.contains("<meta property=\"og:title\" content=\"404 not found\"> ")
                    || html.contains("<meta property=\"og:desc\" content=\"404 not found\">")) {
                resultData.setError(new Error(404, "404 page not found"));
                resultData.setVideos(null);
                return resultData;
            }

            this.uid = getMuid();
            JsonObject videoData = this.parserJsonObject(matchVideoData(html));
            String vid = videoData.get("vid").getAsString();
            this.muid = getMuid();
            initCookies();

            String cateCode = videoData.get("cateCode").getAsString();
            String pid = videoData.get("pid").getAsString();
            String tvid = videoData.get("tvid").getAsString();
            String channeled = videoData.get("channeled").getAsString();
            String nid = videoData.get("nid").getAsString();

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
                        sort(videos);
                        resultData.setVideos(videos);

                        // 获取成功后依次请求 大概是认证SOHUSVP参数
                        sendRequest(String.format(PV_URL, url, srcURL, uid, screen, cateCode, pid, vid, tvid, String.valueOf(System.currentTimeMillis()), channeled));
                        sendRequest(IRT_URL);
                        sendRequest(String.format(ATY_URL, String.valueOf(System.currentTimeMillis()), uid, nid, url, srcURL, screen, vid, pid, channeled));

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
                    .timeout(TIMEOUT).ignoreContentType(true).execute();
            Map<String, String> cookies = response.cookies();
            if (cookies != null && !cookies.isEmpty()) {
                if (cookies.containsKey("SOHUSVP")) {
                    this.cookies.put("SOHUSVP", cookies.get("SOHUSVP"));
                }
            }
            return response.body();
        } catch (IOException e) {
            throw e;
        }
    }

    private void sendRequest(String url) throws IOException {
        try {
            Map<String, String> headers = setHeaders();
            headers.put("Connection", "keep-alive");
            headers.put("Pragma", "no-cache");
            headers.put("Cache-Control", "no-cache");
            headers.put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            headers.put("Referer", srcURL.replace("http://tv", "http://m.tv"));
            if (url.contains("sohu.irs01.com")) {
                Jsoup.connect(url)
                        .headers(headers)
                        .timeout(TIMEOUT).ignoreContentType(true).execute();
            } else {
                Jsoup.connect(url)
                        .headers(headers)
                        .cookies(this.cookies)
                        .timeout(TIMEOUT).ignoreContentType(true).execute();
            }
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
                .timeout(TIMEOUT).get();
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
        } else if ("hig".equals(streamType)) {
            return "h2";
        } else if ("nor".equals(streamType)) {
            return "h3";
        } else {
            return "h1";
        }
    }

}
