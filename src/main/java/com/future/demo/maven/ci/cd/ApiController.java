package com.future.demo.maven.ci.cd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dexterleslie@gmail.com
 */
@RestController
public class ApiController {
    private final static Logger logger = LoggerFactory.getLogger(ApiController.class);

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value="/")
    public ResponseEntity<String> test1(
            HttpServletRequest request,
            HttpServletResponse response){
        return ResponseEntity.ok("This maven project is mainly using for demonstrating CI/CD processes.");
    }

    /**
     * 用于检测应用是否准备就绪
     * @return
     */
    @RequestMapping(value = "/status")
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("Ready");
    }
}
