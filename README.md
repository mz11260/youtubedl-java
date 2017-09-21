# youtubedl-java

### youtubedl-java是什么？
youtubedl-java是根据开源项目youtube-dl实现的获取视频网站真实播放地址的工具。
工具依赖于jsoup-1.10.2和gons-2.8.0，适合Android移动端调用。
目前仅实现了获取播放地址，支持的网站有youku、aqiyi、mgtv。
> `Tips`:
[了解youtube-dl](http://rg3.github.io/youtube-dl/)
[youtube-dl github地址](https://github.com/rg3/youtube-dl/)
>

### 如何使用
- 拉取源码，使用maven编译打包。
- 将编译好的jar包引入您的项目，如果您的项目是通过maven构建的直接在pom.xml文件中添加以下配置。
```xml
<dependency>
    <groupId>org.youtubedl</groupId>
    <artifactId>youtubedl-java</artifactId>
    <version>0.0.43</version>
</dependency>
```
#### java中调用
```java
package youtubedl.test;

import java.util.List;

import YoutubeDL;
import ResultData;
import Video;
import Error;

public class Test2 {
	
	public static void main(String[] args) {
		String[] urls = new String[]{
				"http://www.iqiyi.com/v_19rrb3xn68.html",
				"http://www.mgtv.com/b/313299/3885404.html",
				"http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html"};
		for (String url : urls) {
			System.out.println("解析地址：" + url);
			ResultData data = (ResultData) YoutubeDL.parser(url);
			Error e = data.getError();
			if (e == null) {
			    // 获取到的播放链接是一个集合
			    // 安照hdtv(清晰度)由低到高排序
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
}
```
#### Android中调用
```java
package com.example.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import YoutubeDL;
import Error;
import ResultData;
import Video;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnParse;
    private TextView tvParseResult;
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse = (Button) findViewById(R.id.btn_parse);
        tvParseResult = (TextView) findViewById(R.id.tv_parse_result);
        etUrl = (EditText) findViewById(R.id.et_url);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse();
            }
        });
    }

    private String mUrl;

    private void parse() {
        mUrl = etUrl.getText().toString().trim();
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://v.youku.com/v_show/id_XNTQwMTgxMTE2.html";
        }
        AsyncTask<Void, Void, List<String>> parseTask = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                ResultData resultData = YoutubeDL.parser(mUrl);
                Error error = resultData.getError();
                List<String> resultList = new ArrayList<>();
                if (error == null) {
                    List<Video> videoList = resultData.getVideos();
                    for (Video video : videoList) {
                        resultList.add(video.getPlayUrl());
                    }
                }
                return resultList;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                // super.onPostExecute(strings);
                tvParseResult.setText("真实播放地址：\n" + strings.toString());
            }
        };

        parseTask.execute();

    }
}
```