/**
 *
 * @Bignon
 */
package com.bootcamp.rest.Designs;

public class Critere {
    private Pagination pagination;
    private Sort sort;
    
    private boolean isDefine;

    public Critere() {
        this.isDefine = false;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
        this.isDefine = true;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
        this.isDefine = true;
    }

    public boolean isDefine() {
        return isDefine;
    }

    public void setIsDefine(boolean isDefine) {
        this.isDefine = isDefine;
    }
     
}
