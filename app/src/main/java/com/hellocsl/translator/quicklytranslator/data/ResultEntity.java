package com.hellocsl.translator.quicklytranslator.data;

import java.util.List;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/11/6 0006.
 */
public class ResultEntity extends BaseEntity {

    /**
     * from : en
     * to : zh
     * trans_result : [{"src":"today","dst":"今天"}]
     */

    private String from;
    private String to;
    /**
     * src : today
     * dst : 今天
     */

    private List<TransResultEntity> trans_result;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setTrans_result(List<TransResultEntity> trans_result) {
        this.trans_result = trans_result;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<TransResultEntity> getTrans_result() {
        return trans_result;
    }

    public static class TransResultEntity {
        private String src;
        private String dst;

        public void setSrc(String src) {
            this.src = src;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }

        public String getSrc() {
            return src;
        }

        public String getDst() {
            return dst;
        }

        @Override
        public String toString() {
            return "TransResultEntity{" +
                    "src='" + src + '\'' +
                    ", dst='" + dst + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return super.toString() + "ResultEntity{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", trans_result=" + trans_result +
                '}';
    }
}
