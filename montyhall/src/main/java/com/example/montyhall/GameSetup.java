package com.example.montyhall;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class GameSetup {

    private Box box1;
    private Box box2;
    private Box box3;
    private List<Box> boxesInGame;
    private static int[] validChoices = { 1, 2, 3 };

    public void resetBoxes() {
        box1 = new Box(false, 1);
        box2 = new Box(false, 2);
        box3 = new Box(false, 3);
        int random = new Random().nextInt(3) + 1;
        switch (random) {
            case 1 -> box1 = new Box(true, random);
            case 2 -> box2 = new Box(true, random);
            case 3 -> box3 = new Box(true, random);
        }
        boxesInGame = List.of(box1, box2, box3);
    }

    public static int[] getValidChoices() {
        return validChoices;
    }
}