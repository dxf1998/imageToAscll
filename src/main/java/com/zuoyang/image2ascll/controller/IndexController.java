package com.zuoyang.image2ascll.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author  左羊
 * @date 2019-07-31 11:53:24
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index()
    {
        return "imageToAscll";
    }
}
