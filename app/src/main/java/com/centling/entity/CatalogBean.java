package com.centling.entity;

import java.util.List;

/**
 * CatalogBean
 * Created by Victor on 15/12/15.
 */

public class CatalogBean {

    private List<ClassListEntity> class_list;

    public void setClass_list(List<ClassListEntity> class_list) {
        this.class_list = class_list;
    }

    public List<ClassListEntity> getClass_list() {
        return class_list;
    }

    public static class ClassListEntity {
        private String gc_id;
        private String gc_name;

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public void setGc_name(String gc_name) {
            this.gc_name = gc_name;
        }

        public String getGc_id() {
            return gc_id;
        }

        public String getGc_name() {
            return gc_name;
        }
    }
}
