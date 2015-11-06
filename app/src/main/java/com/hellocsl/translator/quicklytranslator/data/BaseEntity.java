package com.hellocsl.translator.quicklytranslator.data;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/11/6 0006.
 */
public class BaseEntity {
    private String error_code;//错误码
    private String error_msg;//错误信息：

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "error_code='" + error_code + '\'' +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }
}
