package com.codvision.check.data;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.xujichang.web.WebConst;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 15:06.
 */

public class DataType {
    /**
     * 创建服务器返回的错误信息
     *
     * @param code
     * @param msg
     * @return
     */
    public static String createErrorFromServerRespData(int code, String msg) {
        return createRespData(code, WebConst.StatusCode.STATUS_SERVER_RESP, msg, null);
    }

    public static String createErrorRespData(int code, String msg) {
        return createRespData(0, code, msg, null);
    }

    public static <T> String createRespData(int code, String msg, T data) {
        return createRespData(0, code, msg, data);
    }

    public static <T> String createRespData(int serverCode, int code, String msg, T data) {
        RespData<T> respData = new RespData<T>();
        respData.setCode(code);
        respData.setMsg(msg);
        respData.setData(data);
        if (code == WebConst.StatusCode.STATUS_SERVER_RESP) {
            respData.setServerCode(serverCode);
        }
        respData.setServerCode(serverCode);
        return new Gson().toJson(respData);
    }

    public static <T> RespData<T> parseRespData(String data, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(RespData.class, new Class[]{clazz});
        return new Gson().fromJson(data, type);
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 返回数据的包装类
     *
     * @param <T>
     */
    public static class RespData<T> {
        /**
         *
         */
        protected int code;
        protected String msg;
        protected T data;
        protected int serverCode;

        public int getServerCode() {
            return serverCode;
        }

        public void setServerCode(int serverCode) {
            this.serverCode = serverCode;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    /**
     * 请求的参数
     */
    public static class ReqData {
        protected int option;

        public int getOption() {
            return option;
        }

        public void setOption(int option) {
            this.option = option;
        }
    }

}
