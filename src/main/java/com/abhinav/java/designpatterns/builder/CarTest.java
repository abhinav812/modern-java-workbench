package com.abhinav.java.designpatterns.builder;


public class CarTest {
    public static void main(String[] args) {
        var carBuilder = Car.newBuilder();
        var c = carBuilder.make("Honda")
                .makeYear(1987)
                .engineCapacity("1200 CC")
                .color("red")
                .build();
        System.out.println(c);
    }
}
