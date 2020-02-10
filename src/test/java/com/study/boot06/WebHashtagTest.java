package com.study.boot06;

import com.study.boot06.domain.WebBoard;
import com.study.boot06.domain.WebHashtag;
import com.study.boot06.persistence.WebHashtagRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class WebHashtagTest {

    @Autowired
    WebHashtagRepository repo;

    @Test
    public void testInsertHashtag(){

        Long[] arr = {304L, 303L, 300L};

        Arrays.stream(arr).forEach( tag -> {

            WebHashtag hashtag = new WebHashtag();
            hashtag.setTagName("VoC");
            repo.save(hashtag);

            WebBoard board = new WebBoard();
            board.setBno(tag);
        });
    }
}