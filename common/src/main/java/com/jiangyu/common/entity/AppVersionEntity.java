package com.jiangyu.common.entity;

import java.util.List;

public class AppVersionEntity {


    public DataBean data;
    public String code;
    public String msg;

    public static class DataBean {


        public CurrentVersionBean currentVersion;
        public CurrentVersionBean newVersion;
        public String is_new;
        public List<PayTypeListBean> payTypeList;

        public static class CurrentVersionBean {

            public String package_name;
            public String name;
            public String type;
            public String status;
            public String download_href;
            public String app_desc;
            public String version;
            public String create_time;
            public String is_must;
            public String version_code;
            public String id;
        }

        public static class newVersion {

            public String package_name;
            public String name;
            public String type;
            public String status;
            public String download_href;
            public String app_desc;
            public String version;
            public String create_time;
            public String is_must;
            public String version_code;
            public String id;
        }

        public static class PayTypeListBean {


            public String value;
            public String type;
            public String remark;
            public String create_time;
            public String id;
        }
    }
}
