package com.controller;

import com.annotation.IgnoreAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

    @IgnoreAuth
    @RequestMapping({"/", "/index"})
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/front/index.html");
    }
}
