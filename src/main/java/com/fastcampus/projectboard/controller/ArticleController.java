package com.fastcampus.projectboard.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {


    @GetMapping
    public String index(ModelMap map) {

        map.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleid}")
    public String detail(@PathVariable Long articleid, ModelMap map) {
        map.addAttribute("article", null);
        map.addAttribute("articleComments", List.of());
        return "articles/detail";
    }




}
