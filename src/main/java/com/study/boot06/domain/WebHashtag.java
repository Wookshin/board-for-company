package com.study.boot06.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_webhashtag")
@EqualsAndHashCode(of = "hno")
@ToString(exclude = "boards")
public class WebHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hno;

    private String tagName;

    @CreationTimestamp
    private Timestamp regdate;
    @UpdateTimestamp
    private Timestamp updatedate;

    @ManyToMany(mappedBy = "hashtags")
    private Set<WebBoard> webBoards = new HashSet<>();
}
