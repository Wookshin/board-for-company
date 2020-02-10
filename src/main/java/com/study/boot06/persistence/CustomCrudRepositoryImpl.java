package com.study.boot06.persistence;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.study.boot06.domain.QWebBoard;
import com.study.boot06.domain.QWebReply;
import com.study.boot06.domain.WebBoard;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

@Log
public class CustomCrudRepositoryImpl extends QuerydslRepositorySupport implements CustomWebBoard {


    public CustomCrudRepositoryImpl() {
        super(WebBoard.class);
    }

    @Override
    public Page<Object[]> getCustomPage(String type, String keyword, List<Long> bnoList, Pageable page){

        log.info("================================");
        log.info("TYPE: "+type);
        log.info("KEYWORD: "+keyword);
        log.info("PAGE: "+page);
        log.info("================================");

        QWebBoard b = QWebBoard.webBoard;
        QWebReply r = QWebReply.webReply;

        JPQLQuery<WebBoard> query = from (b);

        JPQLQuery<Tuple> tuple = query.select(b.bno, b.title, r.count(), b.writer, b.regdate);

        tuple.leftJoin(r);
        tuple.on(b.bno.eq(r.board.bno));
        tuple.where(b.bno.gt(0L));

        if(type != null){

            switch (type.toLowerCase()){
                case "t":
                    tuple.where(b.title.like("%"+keyword+"%"));
                    break;
                case "c":
                    tuple.where(b.content.like("%"+keyword+"%"));
                    break;
                case "w":
                    tuple.where(b.writer.like("%" + keyword + "%"));
                    break;
                case "h":
                    tuple.where(b.bno.in(bnoList));
            }
        }

        tuple.groupBy(b.bno);
        tuple.orderBy(b.bno.desc());

        tuple.offset(page.getOffset());
        tuple.limit(page.getPageSize());

        List<Tuple> list = tuple.fetch();

        List<Object[]> resultList = new ArrayList<>();

        list.forEach(t -> {
            resultList.add(t.toArray());
        });

        long total = tuple.fetchCount();

        return new PageImpl<>(resultList, page, total);
    }
}
