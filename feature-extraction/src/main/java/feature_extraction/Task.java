package feature_extraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Task implements CommandLineRunner {
    @Autowired
    private FlowGenerator flowGenerator;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            try {
                flowGenerator.generate(1, 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
