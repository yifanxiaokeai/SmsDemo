package com.jiangyu.common.entity;

public class UserEntity {

    public String status;
    public DataBean data;
    public String orderby;
    public String msg;

    public static class DataBean {
        public UsersBean users;

        public static class UsersBean {
            public String qqId;
            public String id ;
            public String birthday;
            public String sex;
            public String token ;
            public String state;
            public String userName;
            public String wxId;
            public String taobaoId;
            public String header;
            public String mobile;
            public int currency;
            public int integral;
        }
    }
}
