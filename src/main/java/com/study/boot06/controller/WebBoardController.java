package com.study.boot06.controller;

import com.study.boot06.domain.WebBoard;
import com.study.boot06.domain.WebHashtag;
import com.study.boot06.persistence.CustomCrudRepository;
import com.study.boot06.persistence.WebHashtagRepository;
import com.study.boot06.vo.PageMaker;
import com.study.boot06.vo.PageVO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {

    @Autowired
    //private WebBoardRepository repo;
    private CustomCrudRepository boardRepository;

    @Autowired
    private WebHashtagRepository hashtagRepository;

    @GetMapping("/list")
    public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {

        //Page 조회
        Pageable page = vo.makePageable(0, "bno");

        //Page<WebBoard> result = repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
        Page<Object[]> result = boardRepository.getCustomPage(vo.getType(),vo.getKeyword(),null,page);


        log.info("" + page);
        log.info(""+result);

        log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());

        model.addAttribute("result", new PageMaker<>(result));

        //Hashtag 조회
        model.addAttribute("tags", hashtagRepository.findAll().stream().map(WebHashtag::getTagName).collect(Collectors.toList()));
    }

    @GetMapping("/hashtag")
    public String hashtag(String tagName, @ModelAttribute("pageVO") PageVO vo, Model model) {

        log.info("TagName: " + tagName);

        //Page 조회
        Pageable page = vo.makePageable(0, "bno");

        List<Long> bnoList = new ArrayList<>();
        bnoList = hashtagRepository.findByTagName(tagName).getWebBoards().stream().map(WebBoard::getBno).collect(Collectors.toList());

        //Page<WebBoard> result = repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
        Page<Object[]> result = boardRepository.getCustomPage("h",vo.getKeyword(),bnoList, page);


        log.info("" + page);
        log.info(""+result);

        log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());

        model.addAttribute("result", new PageMaker<>(result));

        //Hashtag 조회
        model.addAttribute("tags", hashtagRepository.findAll().stream().map(WebHashtag::getTagName).collect(Collectors.toList()));

        return "/boards/list";
    }

    @GetMapping("/register")
    public void registerGET(@ModelAttribute("vo") WebBoard vo, Model model){
        log.info("register get");
        //Hashtag 조회
        model.addAttribute("tags", hashtagRepository.findAll().stream().map(WebHashtag::getTagName).collect(Collectors.toList()));
    }

    @PostMapping("/register")
    public String registerPOST(@ModelAttribute("vo") WebBoard vo, @RequestParam("tags") String tagName, RedirectAttributes rttr){

        log.info("register post");
        log.info("" + vo);

        WebHashtag webHashtag = hashtagRepository.findByTagName(tagName);
        if(webHashtag == null)
        {
            webHashtag = new WebHashtag();
            webHashtag.setTagName(tagName);
            hashtagRepository.save(webHashtag);
        }

        vo.setHashtags(new HashSet<WebHashtag>(Arrays.asList(webHashtag)));

        boardRepository.save(vo);
        rttr.addFlashAttribute("msg", "success");

        return "redirect:/boards/list";
    }

    @GetMapping("/view")
    public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model){

        log.info("BNO: " + bno);

        boardRepository.findById(bno).ifPresent(board -> {
            Set<WebHashtag> webHashtags = new HashSet<>();
            List<String> tags = new ArrayList<>();
            webHashtags = board.getHashtags();
            for(WebHashtag hashtag : webHashtags){
                tags.add(hashtag.getTagName());
            }

            model.addAttribute("vo", board);
            model.addAttribute("tags", tags);
        });
    }

    @GetMapping("/modify")
    public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model){

        log.info("MODIFY BNO: " + bno);

        boardRepository.findById(bno).ifPresent(board -> {
            Set<WebHashtag> webHashtags = new HashSet<>();
            List<String> tags = new ArrayList<>();
            webHashtags = board.getHashtags();
            for(WebHashtag hashtag : webHashtags){
                tags.add(hashtag.getTagName());
            }

            model.addAttribute("vo", board);
            model.addAttribute("tags", tags);
        });

        model.addAttribute("allTags", hashtagRepository.findAll().stream().map(WebHashtag::getTagName).collect(Collectors.toList()));
    }

    @PostMapping("/delete")
    public String delete(Long bno, PageVO vo, RedirectAttributes rttr){

        log.info("DELETE BNO: " + bno);

        boardRepository.deleteById(bno);

        rttr.addFlashAttribute("msg", "success");

        // 페이징과 검색했던 결과로 이동하는 경우
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword",vo.getKeyword());

        return "redirect:/boards/list";
    }

    @PostMapping("/modify")
    public String modifyPost(WebBoard board, PageVO vo, @RequestParam("tags") String tagName, RedirectAttributes rttr){

        log.info("Modify WebBoard: " + board);

        boardRepository.findById(board.getBno()).ifPresent(origin->{

            origin.setTitle(board.getTitle());
            origin.setContent(board.getContent());

            WebHashtag webHashtag = hashtagRepository.findByTagName(tagName);
            if(webHashtag == null)
            {
                webHashtag = new WebHashtag();
                webHashtag.setTagName(tagName);
                hashtagRepository.save(webHashtag);
            }

            origin.setHashtags(new HashSet<WebHashtag>(Arrays.asList(webHashtag)));
            boardRepository.save(origin);
            rttr.addFlashAttribute("msg", "success");
            rttr.addAttribute("bno", origin.getBno());
        });

        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type",vo.getType());
        rttr.addAttribute("keyword",vo.getKeyword());

        return "redirect:/boards/view";
    }
}
