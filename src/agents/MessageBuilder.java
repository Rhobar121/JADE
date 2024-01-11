package agents;

import java.util.ArrayList;

public class MessageBuilder {
    public static String responseBuilder(CarActions action, ArrayList<Car> cars){
        StringBuilder message = new StringBuilder();
        message.append(action);
        message.append("::");
        for (Car car : cars){
            message.append(car.toString());
            message.append(";");
        }
        return message.toString();
    }

    public static String responseBuilder(CarActions action, Car car){
        return action +
                "::" +
                car +
                ";";
    }
}
