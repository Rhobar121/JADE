package agents;

import java.util.Objects;

public class Car {
    String brand;
    String model;
    String engine;
    int capacity;
    int yearOfProduction;
    float cost;
    float extraCost;

    public Car(String brand, String model, String engine, int capacity, int yearOfProduction){
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.capacity = capacity;
        this.yearOfProduction =yearOfProduction;
        this.cost = Float.MIN_VALUE;
        this.extraCost = Float.MIN_VALUE;
    }
    public Car(String brand, String model, String engine, int capacity, int yearOfProduction, float cost, float extraCost){
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.capacity = capacity;
        this.yearOfProduction =yearOfProduction;
        this.cost = cost;
        this.extraCost =extraCost;
    }

    public float getTotalPrice(){
        return cost + extraCost;
    }

    @Override
    public boolean equals(Object object){
        if(this == object) {
            return true;
        }
        if(object == null){
            return false;
        }

        if(object.getClass() != this.getClass()){
            return false;
        } else {
            Car car = (Car) object;
            return Objects.equals(this.brand, car.brand)
                    && Objects.equals(this.model, car.model)
                    && Objects.equals(this.engine, car.engine)
                    && this.capacity == car.capacity
                    && this.yearOfProduction == car.yearOfProduction;
        }
    }
    @Override
    public String toString(){
        if(cost == Float.MIN_VALUE){
            return brand +
                    " " +
                    model +
                    " " +
                    engine +
                    " " +
                    capacity +
                    " " +
                    yearOfProduction;
        } else{
            return brand +
                    " " +
                    model +
                    " " +
                    engine +
                    " " +
                    capacity +
                    " " +
                    yearOfProduction +
                    " " +
                    cost +
                    " " +
                    extraCost +
                    " ";
        }
    }

    public static Car parseString(String string){
        try{
            String val = string.replace(";","");
            String[] temp = val.split(" ");
            if(temp.length==7){
                return new Car(
                        temp[0],
                        temp[1],
                        temp[2],
                        Integer.parseInt(temp[3]),
                        Integer.parseInt(temp[4]),
                        Float.parseFloat(temp[5]),
                        Float.parseFloat(temp[6]));
            } else {
                return new Car(
                        temp[0],
                        temp[1],
                        temp[2],
                        Integer.parseInt(temp[3]),
                        Integer.parseInt(temp[4]));
            }
        } catch (Exception e){
            return null;
        }
    }

    public static Car[] splitStringAndGetCars(String string, String regex){
        String [] args = string.split(regex);
        Car [] cars = new Car[args.length];
        for(int i=0;i< args.length;i++){
            cars[i] = parseString(args[i]);
        }
        return cars;
    }
}
