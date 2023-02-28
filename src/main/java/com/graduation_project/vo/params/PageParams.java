package com.graduation_project.vo.params;

import lombok.Data;

@Data
public class PageParams {
    private int page  = 1;

    private int pageSize = 5;

    private String categoryId = "";

    private String tagId = "";

    private String keyword = "";
}
