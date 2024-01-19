package agents;

import java.util.Objects;

public class Car {
    String brand;
    String model;
    String engine;
    String body;
    int capacity;
    int yearOfProduction;
    int cost;
    int extraCost;

    public Car(String brand, String model, String body, String engine, int capacity, int yearOfProduction){
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.capacity = capacity;
        this.body = body;
        this.yearOfProduction =yearOfProduction;
        this.cost = Integer.MIN_VALUE;
        this.extraCost = Integer.MIN_VALUE;
    }
    public Car(String brand, String model, String body, String engine, int capacity, int yearOfProduction, int cost, int extraCost){
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.body = body;
        this.capacity = capacity;
        this.yearOfProduction =yearOfProduction;
        this.cost = cost;
        this.extraCost =extraCost;
    }

    public int getTotalPrice(){
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
                    && this.yearOfProduction == car.yearOfProduction
                    && Objects.equals(this.body,car.body);
        }
    }
    @Override
    public String toString(){
        if(cost == Float.MIN_VALUE){
            return brand +
                    " " +
                    model +
                    " " +
                    body +
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
                    body +
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
            if(temp.length==8){
                return new Car(
                        temp[0],
                        temp[1],
                        temp[2],
                        temp[3],
                        Integer.parseInt(temp[4]),
                        Integer.parseInt(temp[5]),
                        Integer.parseInt(temp[6]),
                        Integer.parseInt(temp[7]));
            } else {
                return new Car(
                        temp[0],
                        temp[1],
                        temp[2],
                        temp[3],
                        Integer.parseInt(temp[4]),
                        Integer.parseInt(temp[5]));
            }
        } catch (Exception e){
            return null;
        }
    }

    public static Car[] splitString(String string, String regex){
        String [] args = string.split(regex);
        Car [] cars = new Car[args.length];
        for(int i=0;i< args.length;i++){
            cars[i] = parseString(args[i]);
        }
        return cars;
    }
}
