package org.youtubedl;

import org.youtubedl.pojo.ResultData;


/**
 * URL 解析器接口
 * 相应网站的播放地址解析实现该接口即可
 *
 */
public interface URLParser {
	
	/**
	 * 解析 URL 得到相应的视频信息
	 * 
	 * @param  url 源地址
	 * @return 解析后的数据对象
	 */
	ResultData parse(String url);

}
