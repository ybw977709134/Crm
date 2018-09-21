package com.onemeter.entity;

import java.util.List;

/**
 * 描述：
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/13 19:40
 * 备注：
 */
public class new_courseInfo {
    private String code;
    private int total;
    private String page;
    private int type;
    private String keyword;
    private String pageSize;
    public List<mdatas> datas;

    public class mdatas {
        private String user_id;
        private String id;
        private String name;
        private String code;
        private String price;
        private String hours;
        private String ctime;
        private String status;
        private String  desc;
        public List<mimgs> imgs;

        public class mimgs {
            private String src;

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<mimgs> getImgs() {
            return imgs;
        }

        public void setImgs(List<mimgs> imgs) {
            this.imgs = imgs;
        }
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<mdatas> getDatas() {
        return datas;
    }

    public void setDatas(List<mdatas> datas) {
        this.datas = datas;
    }
}
