package com.rtu.chalkac.global.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Getter
public class PageableDto<T> {
    private long page;
    private long size;
    private boolean start;
    private boolean end;
    private List<T> content;

    public PageableDto(Page<T> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.start = page.isFirst();
        this.end = page.isLast();
        this.content = page.getContent();
    }
}
