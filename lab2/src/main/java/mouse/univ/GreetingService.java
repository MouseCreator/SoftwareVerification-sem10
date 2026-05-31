package mouse.univ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    @Autowired
    private DatabaseService databaseService;

    public String greet(String name) {
        int number = databaseService.getNumberOfApples(name);
        return "Hello, " + name + "! You have " + number + " apples.";
    }
}
