package com.example.montyhall;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class GameService {
   
   private final GameSetup gameSetup;

    public GameService(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    public void setupNewGame() {
        gameSetup.resetBoxes();
    }

    public void chooseBox(int choice) {
        gameSetup.getBoxesInGame()
                .forEach(box -> box.setChosen(Objects.equals(box.getIndex(), choice)));
    }

    public List<Box> getActiveBoxes() {
        return gameSetup.getBoxesInGame().stream()
                .filter(Box::isActive)
                .toList();
    }

    public int deactivateOneEmptyNonchoosedBox() {
        return getNonChosenBoxes().stream()
                .filter(box -> !box.isContainsWin())
                .limit(getUnchosenBoxesSize())
                .skip(getSkipIndex())
                .peek(box -> box.setActive(false))
                .map(Box::getIndex)
                .findFirst()
                .get();
    }

    public long getUnchosenBoxesSize() {
        return getNonChosenBoxes().stream()
                .filter(box -> !box.isContainsWin())
                .count();
    }

    public boolean isValidChoice(int choice) {
        return IntStream.of(GameSetup.getValidChoices())
                .anyMatch(x -> x == choice);
    }

    public int getFirstRemainingBoxIndex() {
        return getActiveBoxes().stream()
                .filter(box -> !box.isChosen())
                .map(box -> box.getIndex())
                .findFirst()
                .get();
    }

    public Box getBoxByChoice(int choice) {
        return gameSetup.getBoxesInGame().stream()
                .filter(box -> Objects.equals(box.getIndex(), choice))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No box found with the given choice: " + choice));
    }

    public List<Box> getNonChosenBoxes() {
        return gameSetup.getBoxesInGame().stream()
                .filter(box -> box.isActive() && !box.isChosen())
                .toList();
    }

    protected long getSkipIndex() {
        return getUnchosenBoxesSize() > 1 ? new Random().nextInt(2) : 0;
    }
}