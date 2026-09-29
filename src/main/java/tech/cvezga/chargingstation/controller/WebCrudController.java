package tech.cvezga.chargingstation.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.cvezga.chargingstation.webcrud.WebCrudService;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebCrudController {

    private final WebCrudService webCrudService;

    public WebCrudController(WebCrudService webCrudService) {
        this.webCrudService = webCrudService;
    }

    @GetMapping("/form/{entity}")
    @ResponseBody
    public String userForm(@PathVariable String entity) throws ClassNotFoundException {
        return webCrudService.getList(entity);
    }

    @PostMapping("/entity/save")
    @ResponseBody
    public String save(HttpServletRequest httpServletRequest) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        httpServletRequest.getParameterNames().asIterator().forEachRemaining( param ->{
            map.put(param, httpServletRequest.getParameter(param));
        });
        return webCrudService.save(map);
    }
}
