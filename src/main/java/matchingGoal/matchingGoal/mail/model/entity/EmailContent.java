package matchingGoal.matchingGoal.mail.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class EmailContent {
    private Map<String, String> variables;

    public EmailContent() {
        this.variables = new HashMap<>();
    }

    public void add(String key, String value) {
        this.variables.put(key, value);
    }
}