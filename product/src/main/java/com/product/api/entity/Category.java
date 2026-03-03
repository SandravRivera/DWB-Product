package com.product.api.entity;

public class Category {
    private Integer category_id;
    private String category;
    private String tag;
    private Integer status;

    public Category(Integer category_id, String category, String tag, Integer status) {
        this.category_id = category_id;
        this.category = category;
        this.tag = tag;
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" + category_id + ", '" + category + "', '" + tag + "', " + status + "}";
    }

    /******************
     * SETTERS
     ******************/
    
    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /******************
     * GETTERS
     ******************/

    public Integer getCategory_id() {
        return category_id;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public Integer getStatus() {
        return status;
    }

}