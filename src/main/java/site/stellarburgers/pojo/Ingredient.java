package site.stellarburgers.pojo;

import lombok.Data;

@Data
public class Ingredient {
    private final String _id;
    private final String name;
    private final String type;
    private final int calories;
    private final int price;
}
