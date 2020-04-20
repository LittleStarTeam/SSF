package com.starteam.network.api;


import com.starteam.network.response.IResponse;

import org.json.JSONObject;

public class Response<T> implements IResponse<T> {
    public String msg = "系统繁忙,请稍后重试!";
    public int state = 0;
    private T data;

    public T getContent() {
        return data;
    }

    public void setContent(T data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return state == 0;
    }

    public String getMessage() {
        return msg;
    }

    @Override
    public int getCode() {
        return state;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SuppressWarnings("unchecked")
    public Response<T> parserObject(String json) {
        try {
            JSONObject root = new JSONObject(json);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            this.data = (T) root.opt("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Response<T> parserObject2String(String json) {
        try {
            JSONObject root = new JSONObject(json);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            if (root.opt("data") instanceof String) {
                this.data = (T) root.opt("data");
            } else {
                if (root.opt("data") == null) {
                    this.data = (T) "";
                } else {
                    this.data = (T) String.valueOf(root.opt("data"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<T> parserObject2Integer(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            if ((root.opt("data") instanceof Integer)) {
                this.data = (T) root.opt("data");
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<T> parserObject2Boolean(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            if ((root.opt("data") instanceof Boolean)) {
                this.data = (T) root.opt("data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<T> parserObject2Double(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            if ((root.opt("data") instanceof Double)) {
                this.data = (T) root.opt("data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<T> parserObject2Long(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            this.state = root.optInt("state");
            this.msg = root.optString("msg");
            if ((root.opt("data") instanceof Long)) {
                this.data = (T) root.opt("data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
