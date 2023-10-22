package com.example.montyhall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GameServiceTests {

    GameSetup gameSetup;
    GameService gameService;
    Box box1;
    Box box2;
    Box box3;

    @BeforeEach
    void testSetup() {
        gameSetup = new GameSetup();
        gameService = new GameService(gameSetup);
        gameService.setupNewGame();
    }

    void setupBoxes1WinAndChosen() {
        box1 = new Box(true, 1);
        box1.setChosen(true);
        box2 = new Box(false, 2);
        box3 = new Box(false, 3);
        gameSetup.setBoxesInGame(List.of(box1, box2, box3));
    }

    static Stream<Arguments> choices() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3));
    }

    @ParameterizedTest
    @MethodSource("choices")
    void testChooseBox(int choice) {
        gameService.chooseBox(choice);
        assertTrue(gameService.getBoxByChoice(choice).isChosen());

        List.of(1, 2, 3)
                .stream()
                .filter(i -> i != choice)
                .forEach(i -> assertFalse(gameService.getBoxByChoice(i).isChosen()));
    }

    @Test
    void testGetActiveBoxes() {
        assertEquals(3, gameService.getActiveBoxes().size());
        gameService.getBoxByChoice(1).setActive(false);
        assertEquals(2, gameService.getActiveBoxes().size());
    }

    @Test
    void testGetBoxByChoice() {
        Box box = new Box(false, 9);
        gameSetup.setBox1(box);
        gameSetup.setBoxesInGame(List.of(box, gameService.getBoxByChoice(2), gameService.getBoxByChoice(3)));
        assertEquals(box, gameService.getBoxByChoice(9));

        assertThrows(IllegalArgumentException.class,() -> {
            gameService.getBoxByChoice(54);
        });
    }

    @Test
    void testGetFirstRemainingBoxIndex() {
        setupBoxes1WinAndChosen();
        box2.setActive(false);

        assertEquals(3, gameService.getFirstRemainingBoxIndex());
    }

    @Test
    void testGetNonChosenBoxes() {
        setupBoxes1WinAndChosen();
        assertEquals(List.of(box2, box3), gameService.getNonChosenBoxes());
    }

    static Stream<Arguments> validChoiceProvider() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(2, true),
                Arguments.of(3, true),
                Arguments.of(4, false));
    }

    @ParameterizedTest
    @MethodSource("validChoiceProvider")
    void testIsValidChoice(int choice, boolean expected) {
        assertEquals(expected, gameService.isValidChoice(choice));
    }

    @Test
    void testDeactivateOneEmptyNonchoosedBox() {
        setupBoxes1WinAndChosen();
        box1.setChosen(false);
        box2.setChosen(true);

        gameService.deactivateOneEmptyNonchoosedBox();
        assertFalse(box3.isActive());
    }

    @Test
    void testGetUnchosenBoxesSize() {
        setupBoxes1WinAndChosen();

        assertEquals(2, gameService.getUnchosenBoxesSize());

        box2.setChosen(true);

        assertEquals(1, gameService.getUnchosenBoxesSize());
    }

    @Test
    void testGetSkipIndex() {
        Set<Long> observedSkipIndexes = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            observedSkipIndexes.add(gameService.getSkipIndex());
        }
        assertEquals(Set.of(0L, 1L), observedSkipIndexes);
    }
}
