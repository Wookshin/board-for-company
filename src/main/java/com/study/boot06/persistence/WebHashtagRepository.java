package com.study.boot06.persistence;

import com.study.boot06.domain.WebBoard;
import com.study.boot06.domain.WebHashtag;
import com.study.boot06.domain.WebReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface WebHashtagRepository extends CrudRepository<WebHashtag, Long> {

    WebHashtag findByTagName(String tagName);

    @Override
    List<WebHashtag> findAll();
}
