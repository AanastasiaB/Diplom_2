package site.stellarburgers.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private final List<String> ingredients;
}