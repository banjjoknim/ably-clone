package com.softsquared.template.config;

import lombok.Getter;

import static com.softsquared.template.config.Constant.DEFAULT_PAGING_SIZE;

@Getter
public class PageRequest {

    private int page;
    private int size;

    public void setPage(int page) {
        if (page <= 1) {
            this.page = 0;
            return;
        }
        this.page = page - 1;
    }

    public void setSize(int size) {
        if (size <= 0) {
            this.size = DEFAULT_PAGING_SIZE;
            return;
        }
        this.size = size;
    }

    public PageRequest(int page, int size) {
        setPage(page);
        setSize(size);
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page, size);
    }
}
