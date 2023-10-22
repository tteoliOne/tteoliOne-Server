package store.tteolione.tteolione;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    public String test() {
        return "testSuccess";
    }
}
