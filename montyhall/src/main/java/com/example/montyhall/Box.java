package com.example.montyhall;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Box {

    private boolean containsWin;
    private boolean isChosen;
    private boolean isActive;

    private int index;

    public Box(boolean containsWin, int index) {
        this.isChosen = false;
        this.isActive = true;
        this.containsWin = containsWin;
        this.index = index;
    }
}
