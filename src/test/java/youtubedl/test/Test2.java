package youtubedl.test;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.youtubedl.YoutubeDL;
import org.youtubedl.pojo.Error;
import org.youtubedl.pojo.ResultData;
import org.youtubedl.pojo.Video;

import java.util.Iterator;
import java.util.List;

public class Test2 {
	
	public static void main(String[] args) {
		/*test();
		testList();*/
		String[] urls = new String[]{
				"http://v.youku.com/v_show/id_XMjY0MzY0MDM4OA==.html"
				//"http://v.youku.com/v_show/id_XMTgyNTgwODQ0MA==.html"
				//"http://www.iqiyi.com/v_19rrkdlco0.html"
				//"http://www.mgtv.com/b/316387/4103109.html"
				//"http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html",
				//"http://www.le.com/ptv/vplay/832992.html"
		        //"http://tv.sohu.com/20170616/n600005453.shtml"
		};
		//String url = "http://www.iqiyi.com/v_19rrb3xn68.html";
		//String url = "http://www.mgtv.com/b/313299/3885404.html";
		//String url = "http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html";
		for (String url : urls) {
			System.out.println("解析地址：" + url);
			ResultData data = (ResultData) YoutubeDL.parser(url);
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
	}

	public static void test() {
		String url = "http://www.mgtv.com/b/313299/3885404.html";
		String result = YoutubeDL.parserJson(url);
		System.out.println(result);
		if (result != null) {
			JsonArray array = (JsonArray) new JsonParser().parse(result);
			Iterator iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonObject obj = (JsonObject) iterator.next();
				System.out.println(obj.get("playUrl").getAsString());
			}
		}
	}

	public static void testList() {
		String url = "http://www.mgtv.com/b/313299/3885404.html";
		List<String> result = YoutubeDL.parserList(url);
		if (result != null) {
			for (String s : result) {
				System.out.println(s);
			}
		}
	}
}

/**
 * …or create a new repository on the command line

 echo "# youtubedl-java" >> README.md
 git init
 git add README.md
 git commit -m "first commit"
 git remote add origin https://github.com/Andy11260/youtubedl-java.git
 git push -u origin master
 */
