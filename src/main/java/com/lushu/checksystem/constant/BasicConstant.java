package com.lushu.checksystem.constant;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/12
 * @throws
 * @since
 */
public interface BasicConstant {

    enum User implements BasicConstant{
        //实体类的字段
        USERNAME("username"),
        PASSWORD("password"),
        REAL_NAME("realname"),
        DEPARTMENT("department"),
        MAJOR("major"),
        CREATE_TIME("createTime"),
        LAST_LOGIN_TIME("lastLoginTime"),

        ACCOUNT_NON_EXPIRED("accountNonExpired"),
        ACCOUNT_NON_LOCKED("accountNonLocked"),
        CREDENTIALS_NON_EXPIRED("credentialsNonExpired"),
        ENABLED("enabled"),

        AUTHORITIES("authorities");

        private String string;

        User(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    enum FileAction implements BasicConstant{
        //重命名
        RENAME("RENAME"),
        //移动
        MOVE("MOVE"),
        //批改
        CORRECT("CORRECT");

        private String string;

        public String getString() {
            return string;
        }

        FileAction(String string) {
            this.string = string;
        }
    }

}
