package mouse.univ;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final static long MAX_ALLOWED = 10_000;

    @GetMapping("/api/product")
    public ResponseEntity<String> getProduct(@RequestParam("a") Long a, @RequestParam("b") Long b) {
        if (a == null || a < -MAX_ALLOWED || a > MAX_ALLOWED ) {
            return new ResponseEntity<>("Invalid value for parameter a", HttpStatus.OK);
        }
        if (b == null || b < -MAX_ALLOWED || b > MAX_ALLOWED ) {
            return new ResponseEntity<>("Invalid value for parameter b", HttpStatus.OK);
        }
        return new ResponseEntity<>(String.valueOf(a * b), HttpStatus.OK);
    }
}
