package com.example.montyhall;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MontyhallGame {
    
    @Autowired
    private GameService gameService;

    Scanner scanner = new Scanner(System.in);
    int choice;
    int nonChosenBox;
    boolean changedChoice = false;
    boolean wantToPlay = true;
    

    public void startGame() {

        while (wantToPlay) {

            gameService.setupNewGame();

            printWelcomeText();
            choice = getChoice();

            revealOneEmptyBox();

            nonChosenBox = gameService.getFirstRemainingBoxIndex(); 

            askToChangeBox();

            printLetsSeeIfYouWon();

            printWinOrLoose();

            askToPlayAgain();
        }
    }

    private void printWelcomeText() {
        System.out.println("""
            *********************************************
                Welcome to the Monty Hall game!
            *********************************************

                You will now get to pick 1 out of 3 Boxs. Behind one of the Boxs there will be a prize!
                Which Box will you take?

                Box 1           Box 2           Box 3

                Insert 1, 2 or 3 to choose.
                """);
    }

    private int getChoice() {
        int choice = getValidChoice();

        gameService.chooseBox(choice);

        System.out.println(String.format("""
                You chose Box %s
                """, choice));

        return choice;
    }

    private int getValidChoice() {
        int ret;
        // Validate the input until it is correct
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Please insert 1, 2 or 3");
        }

        ret = scanner.nextInt();
        if (!gameService.isValidChoice(ret)) {
            System.out.println("Please insert 1, 2 or 3");
            ret = getValidChoice();
        }
        return ret;
    }

    private void revealOneEmptyBox() {
        int removedBox = gameService.deactivateOneEmptyNonchoosedBox();

        System.out.println(String.format("""

                I will now reveal to you that Box %s does not contain the prize!
                        """, removedBox));
    }

    private void askToChangeBox() {
        System.out.println(String.format("You have chosen Box %s.", choice));

        String changeBoxChoice = getChangeBoxChoice();

        if (changeBoxChoice.startsWith("y")) {
            int temp = choice;
            choice = nonChosenBox;
            nonChosenBox = temp;
            gameService.chooseBox(choice);
            System.out.println("You chose to change you choice!");
        } else {
            System.out.println("You chose to keep your choice!");
        }
    }

    private String getChangeBoxChoice() {
        return Stream.generate(() -> {
            System.out.println(
                    String.format("Do you want to change your choice to Box %s? Yes (y) or no (n)", nonChosenBox));
            scanner.nextLine(); // Consume the emptyline input that println inputs
            return scanner.nextLine();
        })
                .filter(this::isYesOrNo)
                .findFirst()
                .orElseGet(() -> getChangeBoxChoice())
                .toLowerCase();
    }

    private void printLetsSeeIfYouWon() {
        System.out.println(String.format("""
                Now let us see if you have won..........

                You chose Box %s, and behind that Box is.......
                ........
                """, choice));
    }

    private void printWinOrLoose() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Doesn't really matter in this case, just continue
        }

        System.out.println(gameService.getBoxByChoice(choice).isChosen()
                ? """
                        THE WINNING PRIZE!!!
                        Congratulations you won the Monty Hall game!
                        """
                : String.format(
                        "Some coal, I am sorry that you didn't win. The prize was behind Box %s all along.",
                        nonChosenBox).concat(
                                changedChoice
                                        ? " I guess you shouldn't have changed your choice, even that meant a higher percantage to win."
                                        : ""));

    }

    private void askToPlayAgain() {
        wantToPlay = getPlayAgainChoice().startsWith("y");
    }

    private String getPlayAgainChoice() {
        return Stream.generate(() -> {
            System.out.println("Do you want to play again?");
            return scanner.nextLine();
        })
                .filter(this::isYesOrNo)
                .findFirst()
                .orElseGet(() -> getPlayAgainChoice())
                .toLowerCase();
    }

    private boolean isYesOrNo(String input) {
        return Pattern.matches("^[Y|y][E|e]?[S|s]?$|[N|n][O|o]?", input);
    }
}
