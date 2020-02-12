package com.lushu.checksystem.constant;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/12
 */
public interface DatabaseConstant {


    /**
     * 系统的角色枚举
     */
    enum Role implements DatabaseConstant{
        //三个角色
        ADMIN("ADMIN"),
        TEACHER("TEACHER"),
        STUDENT("STUDENT");
        private String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    enum Authority implements DatabaseConstant{
        //用户增/改/删/查
        ROLE_USER_ADD("ROLE_USER_ADD"),
        ROLE_USER_UPDATE("ROLE_USER_UPDATE"),
        ROLE_USER_DELETE("ROLE_USER_DELETE"),
        ROLE_USER_SELECT("ROLE_USER_SELECT"),

        //文件上传/更新/删除/查询/查重
        ROLE_FILE_UPLOAD("ROLE_FILE_UPLOAD"),
        ROLE_FILE_UPDATE("ROLE_FILE_UPDATE"),
        ROLE_FILE_DELETE("ROLE_FILE_DELETE"),
        ROLE_FILE_SELECT("ROLE_FILE_SELECT"),
        ROLE_FILE_CHECK("ROLE_FILE_CHECK"),

        //角色增/改/删/查
        ROLE_ADD("ROLE_ADD"),
        ROLE_UPDATE("ROLE_UPDATE"),
        ROLE_DELETE("ROLE_DELETE"),
        ROLE_SELECT("ROLE_SELECT");
        private String authority;

        Authority(String authority) {
            this.authority = authority;
        }

        public String getAuthority() {
            return authority;
        }
    }

    enum File implements DatabaseConstant{
        //文件夹和word文档
        DIRECTORY_FILE(0),
        WORD_FILE(1),

        //文档文件的status字段
        UNCHECKED(0),
        PASSED(1),
        CHECKED(2),
        UNPASSED(-1);

        private Integer flag;

        File(Integer flag) {
            this.flag = flag;
        }

        public Integer getFlag() {
            return flag;
        }
    }


}
