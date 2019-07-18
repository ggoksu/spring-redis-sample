package io.pivotal.metapa.example.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class MyController {

    private Log log = LogFactory.getLog(MyController.class);


    @Autowired
    private MyService myService;


    @RequestMapping("/")
    public String index(HttpServletRequest request, @RequestParam(value = "doit", required = false) boolean doit, Model model) throws Exception {
        addAppEnv(request, model);
        if(doit) {
            model.addAttribute("killed", true);
            log.warn("The container is shutting down");
            Runnable killTask = () -> {
                try {
                    String name = Thread.currentThread().getName();
                    log.warn("killing shortly " + name);
                    TimeUnit.SECONDS.sleep(1);
                    log.warn("killed " + name);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(killTask).start();
        }
        return "index";
    }

    private void addAppEnv(HttpServletRequest request, Model model) throws Exception {

        Map<String, Object> modelMap = myService.addAppEnv(request);
        model.addAllAttributes(modelMap);
    }

}
