package com.study.boot06.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="tbl_webboards")
@EqualsAndHashCode(of="bno")
@ToString(exclude = {"replies", "hashtags"})
public class WebBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;

    private String writer;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private Timestamp regdate;
    @UpdateTimestamp
    private Timestamp updatedate;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<WebReply> replies;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "board_hashtag",
        joinColumns = @JoinColumn(name="bno", referencedColumnName = "bno"),
        inverseJoinColumns = @JoinColumn(name = "hno", referencedColumnName = "hno"))
    private Set<WebHashtag> hashtags;
}
