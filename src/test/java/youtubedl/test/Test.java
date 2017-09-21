package youtubedl.test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.youtubedl.YoutubeDL;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;

public class Test {
	
	
	@org.junit.Test
	public void testParser() {
		String url = "http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html";
		ResultData data = (ResultData) YoutubeDL.parser(url);
		List<Video> list = data.getVideos();
		for (Video v : list) {
			System.out.println(v.toString());
		}
	}
	
	@org.junit.Test
	public void test2() throws Exception {
		String url = "http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html";
		Response response = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
                .cookie("cna", "")
                .timeout(10000).ignoreContentType(true).execute();
		
		response.headers();
		Map<String, String> cookies = response.cookies();
		Iterator<Map.Entry<String, String>> iterator = cookies.entrySet().iterator();
		if (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}
	
	@org.junit.Test
	public void test1() {
		System.out.println(1504518638);
		System.out.println(new Date().getTime()/1000);
	}
	
	@org.junit.Test
	public void testGson () {
		String str = "{\n" +
                "    \"e\": {\n" +
                "        \"desc\": \"\",\n" +
                "        \"provider\": \"hsfprovider\",\n" +
                "        \"code\": 0\n" +
                "    },\n" +
                "    \"data\": {\n" +
                "        \"stream\": [\n" +
                "            {\n" +
                "                \"logo\": \"none\",\n" +
                "                \"audio_lang\": \"default\",\n" +
                "                \"media_type\": \"standard\",\n" +
                "                \"subtitle_lang\": \"default\",\n" +
                "                \"segs\": [\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/677109026FF3C71783E1F37B9/030020040059313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=393&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A5e7749fdcce8a9c73c46da5baca6cae5\",\n" +
                "                        \"secret\": \"677109026FF3C71783E1F37B9\",\n" +
                "                        \"total_milliseconds_audio\": 393810,\n" +
                "                        \"fileid\": \"030020040059313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 393666,\n" +
                "                        \"key\": \"5daab90a26b4c098282ca392\",\n" +
                "                        \"size\": 8358692\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6771352D4E94371A4E1B13405/030020040159313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=387&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=Aac6e45c412fcfe9e7f42989ca7ea080f\",\n" +
                "                        \"secret\": \"6771352D4E94371A4E1B13405\",\n" +
                "                        \"total_milliseconds_audio\": 387912,\n" +
                "                        \"fileid\": \"030020040159313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 387933,\n" +
                "                        \"key\": \"371518b956588106261fe28d\",\n" +
                "                        \"size\": 6858418\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/677161587CF4471AB42343E59/030020040259313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=388&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A0d2ae8013fe63d3dfc9ea68db06c16ed\",\n" +
                "                        \"secret\": \"677161587CF4471AB42343E59\",\n" +
                "                        \"total_milliseconds_audio\": 388423,\n" +
                "                        \"fileid\": \"030020040259313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 388400,\n" +
                "                        \"key\": \"23d60e4c166026f4282ca392\",\n" +
                "                        \"size\": 7228342\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/67718D838DF31713218816B8A/030020040359313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=240&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=Af1f74d64f675152985c74c75524fb3b1\",\n" +
                "                        \"secret\": \"67718D838DF31713218816B8A\",\n" +
                "                        \"total_milliseconds_audio\": 240578,\n" +
                "                        \"fileid\": \"030020040359313136C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 240600,\n" +
                "                        \"key\": \"32ebfd92a145369e24132188\",\n" +
                "                        \"size\": 4092924\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stream_type\": \"3gphd\",\n" +
                "                \"width\": 360,\n" +
                "                \"size\": 26538376,\n" +
                "                \"height\": 268,\n" +
                "                \"milliseconds_video\": 1410599,\n" +
                "                \"drm_type\": \"default\",\n" +
                "                \"milliseconds_audio\": 1410723,\n" +
                "                \"m3u8_url\": \"http://pl-ali.youku.com/playlist/m3u8?vid=XNTQwMTgxMTE2&type=mp4&ups_client_netip=58.250.169.7&ups_ts=1504521706&utid=5hs0Eqag9TACATr6qQdX6j9q&ccode=0402&psid=9983d9d37e0f8501ac8bf377bc7f6511&duration=1410&expire=18000&ups_key=270aaf4ca8386b52adbda49abdd0cadc\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"logo\": \"none\",\n" +
                "                \"audio_lang\": \"default\",\n" +
                "                \"media_type\": \"standard\",\n" +
                "                \"subtitle_lang\": \"default\",\n" +
                "                \"segs\": [\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6572D3649314D71E4A6CC4CCA/030002040051AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.flv?ccode=0402&duration=409&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A2081354679e84c26e33e66402f2b1d4f\",\n" +
                "                        \"secret\": \"6572D3649314D71E4A6CC4CCA\",\n" +
                "                        \"total_milliseconds_audio\": 409600,\n" +
                "                        \"fileid\": \"030002040051AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 409600,\n" +
                "                        \"key\": \"31227499e5176dcf261fe28d\",\n" +
                "                        \"size\": 16918346\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6774F1EF56233713ED9866B45/030002040151AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.flv?ccode=0402&duration=408&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A1a9e1fe39d89cc7e96664fd4d5e5a8b5\",\n" +
                "                        \"secret\": \"6774F1EF56233713ED9866B45\",\n" +
                "                        \"total_milliseconds_audio\": 408067,\n" +
                "                        \"fileid\": \"030002040151AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 408067,\n" +
                "                        \"key\": \"d6b8fc68eca98992282ca392\",\n" +
                "                        \"size\": 14025785\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6775A6C85A035714B9A8C527E/030002040251AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.flv?ccode=0402&duration=308&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A88af07682f27e06cb76d504ab7da6fc3\",\n" +
                "                        \"secret\": \"6775A6C85A035714B9A8C527E\",\n" +
                "                        \"total_milliseconds_audio\": 308733,\n" +
                "                        \"fileid\": \"030002040251AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 308733,\n" +
                "                        \"key\": \"f281361d1a43287524132188\",\n" +
                "                        \"size\": 11239326\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6977C55351C3A716B7D19696F/030002040351AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.flv?ccode=0402&duration=283&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=Afaf7ccdc6a7340eeff56227bf91e67fe\",\n" +
                "                        \"secret\": \"6977C55351C3A716B7D19696F\",\n" +
                "                        \"total_milliseconds_audio\": 283980,\n" +
                "                        \"fileid\": \"030002040351AF1A17C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 283867,\n" +
                "                        \"key\": \"e5c25a726a6a507f24132188\",\n" +
                "                        \"size\": 8430481\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stream_type\": \"flvhd\",\n" +
                "                \"width\": 448,\n" +
                "                \"size\": 50613938,\n" +
                "                \"height\": 336,\n" +
                "                \"milliseconds_video\": 1410267,\n" +
                "                \"drm_type\": \"default\",\n" +
                "                \"milliseconds_audio\": 1410380,\n" +
                "                \"m3u8_url\": \"http://pl-ali.youku.com/playlist/m3u8?vid=XNTQwMTgxMTE2&type=flv&ups_client_netip=58.250.169.7&ups_ts=1504521706&utid=5hs0Eqag9TACATr6qQdX6j9q&ccode=0402&psid=9983d9d37e0f8501ac8bf377bc7f6511&duration=1410&expire=18000&ups_key=270aaf4ca8386b52adbda49abdd0cadc\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"logo\": \"none\",\n" +
                "                \"audio_lang\": \"default\",\n" +
                "                \"media_type\": \"standard\",\n" +
                "                \"subtitle_lang\": \"default\",\n" +
                "                \"segs\": [\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/67743D108C73971651C9632C7/030008040051AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=403&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A5b0bd218a8273dab233ae4a7e91438db\",\n" +
                "                        \"secret\": \"67743D108C73971651C9632C7\",\n" +
                "                        \"total_milliseconds_audio\": 403469,\n" +
                "                        \"fileid\": \"030008040051AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 403470,\n" +
                "                        \"key\": \"50f1ae30a8e9401f261fe28d\",\n" +
                "                        \"size\": 31867870\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/69765B98E203971651C962500/030008040151AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=408&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A5a4be0a4eb109759527d17551b7d27cc\",\n" +
                "                        \"secret\": \"69765B98E203971651C962500\",\n" +
                "                        \"total_milliseconds_audio\": 408207,\n" +
                "                        \"fileid\": \"030008040151AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 408208,\n" +
                "                        \"key\": \"e45200ffc6fdb3db24132188\",\n" +
                "                        \"size\": 27579798\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/65743D105D03471453A092E12/030008040251AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=296&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=Aa64161dbced1112e785b05a2e59b31bf\",\n" +
                "                        \"secret\": \"65743D105D03471453A092E12\",\n" +
                "                        \"total_milliseconds_audio\": 296798,\n" +
                "                        \"fileid\": \"030008040251AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 296796,\n" +
                "                        \"key\": \"db16c8aa3f6f24ea261fe28d\",\n" +
                "                        \"size\": 19822271\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"cdn_url\": \"http://vali.cp31.ott.cibntv.net/6977C5486424B71D7E5C727BE/030008040351AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE.mp4?ccode=0402&duration=301&expire=18000&psid=9983d9d37e0f8501ac8bf377bc7f6511&ups_client_netip=58.250.169.7&ups_ts=1504521706&ups_userid=&utid=5hs0Eqag9TACATr6qQdX6j9q&vid=XNTQwMTgxMTE2&vkey=A0991cd0b953ab6afee45acd85db770cf\",\n" +
                "                        \"secret\": \"6977C5486424B71D7E5C727BE\",\n" +
                "                        \"total_milliseconds_audio\": 301906,\n" +
                "                        \"fileid\": \"030008040351AF2531C0E80501705752037AC3-3C56-EA5D-235A-34C4A22E77DE\",\n" +
                "                        \"total_milliseconds_video\": 301635,\n" +
                "                        \"key\": \"1c129bf1466a07f124132188\",\n" +
                "                        \"size\": 17831931\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stream_type\": \"mp4hd\",\n" +
                "                \"width\": 576,\n" +
                "                \"size\": 97101870,\n" +
                "                \"height\": 432,\n" +
                "                \"milliseconds_video\": 1410109,\n" +
                "                \"drm_type\": \"default\",\n" +
                "                \"milliseconds_audio\": 1410380,\n" +
                "                \"m3u8_url\": \"http://pl-ali.youku.com/playlist/m3u8?vid=XNTQwMTgxMTE2&type=mp4&ups_client_netip=58.250.169.7&ups_ts=1504521706&utid=5hs0Eqag9TACATr6qQdX6j9q&ccode=0402&psid=9983d9d37e0f8501ac8bf377bc7f6511&duration=1410&expire=18000&ups_key=270aaf4ca8386b52adbda49abdd0cadc\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"playlog\": {},\n" +
                "        \"preview\": {\n" +
                "            \"timespan\": \"6000\",\n" +
                "            \"thumb\": [\n" +
                "                \"http://g2.ykimg.com/0521000351AF258D6A075406C006CF48\",\n" +
                "                \"http://g3.ykimg.com/0521010351AF258D6A075406C006CF48\",\n" +
                "                \"http://g2.ykimg.com/0521020351AF258D6A075406C006CF48\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"dvd\": {\n" +
                "            \"point\": [\n" +
                "                {\n" +
                "                    \"title\": \"\",\n" +
                "                    \"desc\": \"\",\n" +
                "                    \"start\": \"586856\",\n" +
                "                    \"ctype\": \"standard\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"notsharing\": \"0\"\n" +
                "        },\n" +
                "        \"uploader\": {\n" +
                "            \"uid\": \"UNTQzNjg4NzY0\",\n" +
                "            \"certification\": true,\n" +
                "            \"fan_count\": 0,\n" +
                "            \"crm_level\": 12,\n" +
                "            \"username\": \"回家种番薯\",\n" +
                "            \"reason\": \"官方库\",\n" +
                "            \"show_brand\": 0,\n" +
                "            \"avatar\": {\n" +
                "                \"big\": \"https://r1.ykimg.com/0130391F4550AF3436CD23081A020F50674714-88FF-C8A8-C74F-7A99607C84D4\",\n" +
                "                \"middle\": \"https://r1.ykimg.com/0130391F4550AF3436F692081A020FB0A7A974-B22A-FCF6-7AD1-C8B6803B07B0\",\n" +
                "                \"small\": \"https://r1.ykimg.com/0130391F4550AF343751CF081A020F3371E563-D1F7-9DB2-BDC0-F18AD590792D\",\n" +
                "                \"large\": \"https://r1.ykimg.com/0130391F4550AF3435EB90081A020F0C88560D-B594-7359-0D15-9AB238180D92\"\n" +
                "            },\n" +
                "            \"homepage\": \"http://i.youku.com/u/UNTQzNjg4NzY0\"\n" +
                "        },\n" +
                "        \"videos\": {\n" +
                "            \"next\": {\n" +
                "                \"title\": \"第002话 我是木叶丸\",\n" +
                "                \"encodevid\": \"XNTI4NjExNDA4\",\n" +
                "                \"seq\": \"2\",\n" +
                "                \"vid\": \"132152852\"\n" +
                "            },\n" +
                "            \"list\": [\n" +
                "                {\n" +
                "                    \"title\": \"第001话 漩涡鸣人登场\",\n" +
                "                    \"encodevid\": \"XNTQwMTgxMTE2\",\n" +
                "                    \"seq\": \"1\",\n" +
                "                    \"vid\": \"135045279\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第002话 我是木叶丸\",\n" +
                "                    \"encodevid\": \"XNTI4NjExNDA4\",\n" +
                "                    \"seq\": \"2\",\n" +
                "                    \"vid\": \"132152852\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第003话 宿敌 佐助和小樱\",\n" +
                "                    \"encodevid\": \"XNTI4NjEwODg4\",\n" +
                "                    \"seq\": \"3\",\n" +
                "                    \"vid\": \"132152722\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第004话 试炼 求生演习\",\n" +
                "                    \"encodevid\": \"XNTI4NjEwNzky\",\n" +
                "                    \"seq\": \"4\",\n" +
                "                    \"vid\": \"132152698\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第005话 不及格 卡卡西的结论\",\n" +
                "                    \"encodevid\": \"XNTI4NjExNTMy\",\n" +
                "                    \"seq\": \"5\",\n" +
                "                    \"vid\": \"132152883\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第006话 重要任务 向波之国出发\",\n" +
                "                    \"encodevid\": \"XNTI4NjExNzMy\",\n" +
                "                    \"seq\": \"6\",\n" +
                "                    \"vid\": \"132152933\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第007话 雾之暗杀者\",\n" +
                "                    \"encodevid\": \"XNTI4NjEyMTQw\",\n" +
                "                    \"seq\": \"7\",\n" +
                "                    \"vid\": \"132153035\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第008话 以疼痛起誓的决心\",\n" +
                "                    \"encodevid\": \"XNTI4NjEyNDM2\",\n" +
                "                    \"seq\": \"8\",\n" +
                "                    \"vid\": \"132153109\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第009话 写轮眼卡卡西\",\n" +
                "                    \"encodevid\": \"XNTI4NjEyODYw\",\n" +
                "                    \"seq\": \"9\",\n" +
                "                    \"vid\": \"132153215\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第010话 查克拉森林\",\n" +
                "                    \"encodevid\": \"XNTI4NjEyNzk2\",\n" +
                "                    \"seq\": \"10\",\n" +
                "                    \"vid\": \"132153199\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第011话 英雄的国度\",\n" +
                "                    \"encodevid\": \"XNTI4NjEzMzk2\",\n" +
                "                    \"seq\": \"11\",\n" +
                "                    \"vid\": \"132153349\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第012话 桥上决战 再不斩再现\",\n" +
                "                    \"encodevid\": \"XNTI4NjEzNDAw\",\n" +
                "                    \"seq\": \"12\",\n" +
                "                    \"vid\": \"132153350\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第013话 白的秘术 魔镜冰晶\",\n" +
                "                    \"encodevid\": \"XNTI4NjEzNzIw\",\n" +
                "                    \"seq\": \"13\",\n" +
                "                    \"vid\": \"132153430\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第014话 意外性第一 鸣人参战\",\n" +
                "                    \"encodevid\": \"XNTI4NjE0MTgw\",\n" +
                "                    \"seq\": \"14\",\n" +
                "                    \"vid\": \"132153545\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第015话 视线为零的战斗 封锁写轮眼\",\n" +
                "                    \"encodevid\": \"XNTI4NjEzODQ0\",\n" +
                "                    \"seq\": \"15\",\n" +
                "                    \"vid\": \"132153461\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第016话 被解放的封印\",\n" +
                "                    \"encodevid\": \"XNTI4NjE0OTQ0\",\n" +
                "                    \"seq\": \"16\",\n" +
                "                    \"vid\": \"132153736\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第017话 白色的过去 隐藏的回忆\",\n" +
                "                    \"encodevid\": \"XNTI4NjE0NDIw\",\n" +
                "                    \"seq\": \"17\",\n" +
                "                    \"vid\": \"132153605\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第018话 名为忍者的工具\",\n" +
                "                    \"encodevid\": \"XNTI4NjE0NzE2\",\n" +
                "                    \"seq\": \"18\",\n" +
                "                    \"vid\": \"132153679\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第019话 在雪中散去的再不斩\",\n" +
                "                    \"encodevid\": \"XNTI4NjIwODAw\",\n" +
                "                    \"seq\": \"19\",\n" +
                "                    \"vid\": \"132155200\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"title\": \"第020话 新的篇章 晋级中忍的考试\",\n" +
                "                    \"encodevid\": \"XNTI4NjIwOTg4\",\n" +
                "                    \"seq\": \"20\",\n" +
                "                    \"vid\": \"132155247\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"show\": {\n" +
                "            \"episode_total\": 720,\n" +
                "            \"video_type\": 1,\n" +
                "            \"exclusive\": false,\n" +
                "            \"show_vthumburl_huge\": \"http://r1.ykimg.com/0513400059157647ADBC09F3850AEA6B\",\n" +
                "            \"show_vthumburl\": \"http://r1.ykimg.com/050D0000594B5E6BADBA1F1B5C0C6DAA\",\n" +
                "            \"show_thumburl\": \"http://r1.ykimg.com/050B0000594B5E5DADBA1F1BDD0EC55F\",\n" +
                "            \"copyright\": \"1\",\n" +
                "            \"show_thumburl_huge\": \"http://r1.ykimg.com/050C000059158A5AADBAC3F2F4034732\",\n" +
                "            \"stage\": 1,\n" +
                "            \"id\": 19461,\n" +
                "            \"video_pay\": 0,\n" +
                "            \"title\": \"火影忍者\",\n" +
                "            \"show_thumburl_big_jpg\": \"http://r1.ykimg.com/050C0000594B5E5DADBA1F1BDD0EC55F\",\n" +
                "            \"pay_type\": [],\n" +
                "            \"encodeid\": \"cc001f06962411de83b1\",\n" +
                "            \"pay\": 0,\n" +
                "            \"showcategory\": \"动漫\",\n" +
                "            \"show_videotype\": \"正片\",\n" +
                "            \"showkind\": [\n" +
                "                \"少儿\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"controller\": {\n" +
                "            \"html5_disable\": false,\n" +
                "            \"video_capture\": true,\n" +
                "            \"continuous\": false,\n" +
                "            \"like_disabled\": false,\n" +
                "            \"is_phone_stream\": \"0\",\n" +
                "            \"yi_plus\": false,\n" +
                "            \"circle\": false,\n" +
                "            \"play_mode\": 2,\n" +
                "            \"new_core\": false,\n" +
                "            \"stream_mode\": 1,\n" +
                "            \"app_disable\": false,\n" +
                "            \"download_disable\": true,\n" +
                "            \"share_disable\": false\n" +
                "        },\n" +
                "        \"ups\": {\n" +
                "            \"ups_client_netip\": \"58.250.169.7\",\n" +
                "            \"psid\": \"9983d9d37e0f8501ac8bf377bc7f6511\",\n" +
                "            \"ups_ts\": \"1504521706\"\n" +
                "        },\n" +
                "        \"user\": {\n" +
                "            \"uid\": \"\",\n" +
                "            \"vip\": false,\n" +
                "            \"ip\": \"58.250.169.7\"\n" +
                "        },\n" +
                "        \"network\": {\n" +
                "            \"dma_code\": \"17623\",\n" +
                "            \"area_code\": \"440300\",\n" +
                "            \"country_code\": \"CN\"\n" +
                "        },\n" +
                "        \"video\": {\n" +
                "            \"tags\": [\n" +
                "                \"火影忍者\"\n" +
                "            ],\n" +
                "            \"logo\": \"https://vthumb.ykimg.com/0541010851AF253E6A0A45046172D72F\",\n" +
                "            \"weburl\": \"http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html\",\n" +
                "            \"privacy\": \"anybody\",\n" +
                "            \"userid\": 135922191,\n" +
                "            \"category_id\": 100,\n" +
                "            \"ctype\": \"媒资\",\n" +
                "            \"type\": [\n" +
                "                \"show\",\n" +
                "                \"dvd\",\n" +
                "                \"bVideoinfo\",\n" +
                "                \"interact\",\n" +
                "                \"bullet\"\n" +
                "            ],\n" +
                "            \"id\": 135045279,\n" +
                "            \"videoid_play\": 135045279,\n" +
                "            \"username\": \"回家种番薯\",\n" +
                "            \"title\": \"第001话 漩涡鸣人登场\",\n" +
                "            \"seconds\": 1410.38,\n" +
                "            \"encodeid\": \"XNTQwMTgxMTE2\",\n" +
                "            \"category_letter_id\": \"h\",\n" +
                "            \"stream_types\": {\n" +
                "                \"default\": [\n" +
                "                    \"3gphd\",\n" +
                "                    \"flvhd\",\n" +
                "                    \"mp4hd\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"id\": \"2224\",\n" +
                "                    \"name\": \"日韩\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"cost\": 0.0110000008717179\n" +
                "}";
		
		JsonObject json = new JsonParser().parse(str).getAsJsonObject();
		JsonObject data = json.getAsJsonObject("data");
		JsonObject error = data.getAsJsonObject("error");
		if (error == null) {
			
		}
	}
}
