package com.jsz.partner_backend.common;


import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = -449239231414394159L;
    /**
     * 页面大小
     */
    protected int pageSize;

    /**
     * 第几页
     */
    protected int pageNum;
}
