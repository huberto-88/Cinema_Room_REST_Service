package task;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @GetMapping("/{param}")
    public String returnParam(@PathVariable String param) {
        return param;
    }
}
