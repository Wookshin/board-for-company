package com.study.boot06.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomWebBoard {

    public Page<Object[]> getCustomPage(String type, String keyword, List<Long> bnoList, Pageable page);
}
