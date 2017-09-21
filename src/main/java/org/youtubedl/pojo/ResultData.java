package org.youtubedl.pojo;

import java.util.List;

public class ResultData {
	
	/**
	 * 错误信息
	 */
	private Error error;
	
	/**
	 * 解析结果对象
	 */
	private List<Video> videos;

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	
	
}
