package com.abhinav.java.designpatterns.builder;

public class Car {
    private final String make;
    private final int makeYear;
    private final String engineCapacity;
    private final String color;

    public static class Builder {
        private String make;
        private int makeYear;
        private String engineCapacity;
        private String color;

        private Builder() {}

        public Builder make(String make) {
            this.make = make;
            return this;
        }

        public Builder makeYear(int year) {
            this.makeYear = year;
            return this;
        }

        public Builder engineCapacity(String engineCapacity) {
            this.engineCapacity = engineCapacity;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Car build() {
            return new Car(this);
        }

    }

    private Car(Builder builder) {
        this.make = builder.make;
        this.makeYear = builder.makeYear;
        this.engineCapacity = builder.engineCapacity;
        this.color = builder.color;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", makeYear=" + makeYear +
                ", engineCapacity='" + engineCapacity + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}


