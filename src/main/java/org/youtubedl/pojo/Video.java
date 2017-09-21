package org.youtubedl.pojo;

/**
 * 
 *
 */
public class Video {
	
	/** 视频名称 */
	private String title;
	/** 视频图片 */
	private String logo;
	/** 视频真实播放播放地址 */
	private String playUrl;
	/** 清晰度  h1 > h2 > h3 由高到低 */
	private String hdtv;
	/** 码流类型 */
	private String streamType;
	/** 视频大小 */
	private int size;
	/** 视频时长毫秒值 */
	private int videoMilliseconds;
	/**  视频源地址 */
	private String srcUrl;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getHdtv() {
		return hdtv;
	}
	public void setHdtv(String hdtv) {
		this.hdtv = hdtv;
	}
	public String getStreamType() {
		return streamType;
	}
	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getVideoMilliseconds() {
		return videoMilliseconds;
	}
	public void setVideoMilliseconds(int videoMilliseconds) {
		this.videoMilliseconds = videoMilliseconds;
	}
	public String getSrcUrl() {
		return srcUrl;
	}
	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}
	@Override
	public String toString() {
		return "Video [title=" + title + ", logo=" + logo + ", playUrl="
				+ playUrl + ", hdtv=" + hdtv + ", streamType=" + streamType
				+ ", size=" + size
				+ ", videoMilliseconds=" + videoMilliseconds + ", srcUrl="
				+ srcUrl + "]";
	}
	
	
}
