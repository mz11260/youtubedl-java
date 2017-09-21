package org.youtubedl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlUtil {

	
	public static String getDomain(String url) {
		try {
			Pattern p = Pattern.compile(
					"(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			matcher.find();
			return matcher.group();
		} catch (Exception ex) {
		}
		return null;
	}

	public static String getCompleteDomain(String url) {
		try {
			Pattern p = Pattern.compile(
					"[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			matcher = p.matcher(url);
			matcher.find();
			return matcher.group();
		} catch (Exception e) {
			return null;
		}
	}
}
