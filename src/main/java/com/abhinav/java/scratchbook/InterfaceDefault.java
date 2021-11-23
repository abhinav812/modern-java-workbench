package com.abhinav.java.scratchbook;

@SuppressWarnings("java:S106") // suppressing sonarlint warning
public class InterfaceDefault {

    public static void main(String[] args) {
        var cat = new Cat();
        System.out.println(cat.getSpeed());
        System.out.println(cat.getJumpHeight());

    }
}


interface Walk {
    default int getSpeed() {
        return 5;
    }

}

interface Run {
    default int getSpeed() {
        return 10;
    }
}

interface Hop {
    static int getJumpHeight() {
        return 8;
    }
}

interface Jump {
    static int getJumpHeight() {
        return 12;
    }
}

class Cat implements Walk, Run, Hop, Jump {

    @Override
    public int getSpeed() {
        return Walk.super.getSpeed();
    }

    public int getJumpHeight() {
        return Hop.getJumpHeight();
    }
}