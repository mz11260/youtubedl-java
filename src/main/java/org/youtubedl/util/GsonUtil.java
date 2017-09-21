package org.youtubedl.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by MK on 2017/9/18.
 */
public class GsonUtil {
    /**
     * 创建一个JSON对象
     *
     * @return
     */
    private static final Gson create() {
        return new GsonBuilder()
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    /**
     * 生成JSON字符串
     *
     * @param jsonElement
     * @return
     */
    public static final String toJson(Object jsonElement) {
        return create().toJson(jsonElement);
    }
}
