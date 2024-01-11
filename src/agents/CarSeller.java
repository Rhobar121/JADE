package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class CarSeller extends Agent {
    ArrayList<Car> cars;
    HashMap<Car, Boolean> reservation;
    AID[] aids;

    private void findAction(String request, ACLMessage receivedMessage){
        Car [] requested = Car.splitStringAndGetCars(request,";");
        ArrayList<Car> responseList = new ArrayList<>();
        for (Car value : cars) {
            for (Car requestCar : requested) {
                if (value.equals(requestCar)) {
                    responseList.add(value);
                }
            }
        }
        ACLMessage response = receivedMessage.createReply();
        if(!responseList.isEmpty()){
            String message = MessageBuilder.responseBuilder(CarActions.FIND,responseList);
            response.setContent(message);
        }
        send(response);
    }

    private void cancelAction(String request){
        Car requested = Car.parseString(request);
        for(Car car : cars){
            if(car.equals(requested)){
                reservation.replace(requested,false);
            }
        }
    }

    private void payAction(String request){
        Car requested = Car.parseString(request);
        for(Car car : cars){
            if(car.equals(requested)){
                reservation.remove(requested);
                cars.remove(requested);
            }
        }
    }

    private void sellAction(String request, ACLMessage receivedMessage){
        Car requested = Car.parseString(request);
        ACLMessage response = receivedMessage.createReply();
        String message = null;
        for(Car car : cars){
            if(car.equals(requested)){
                if(!reservation.get(car)){
                    reservation.replace(car,true);
                    message = MessageBuilder.responseBuilder(CarActions.APPROVE,requested);
                } else{
                    message = MessageBuilder.responseBuilder(CarActions.DENIAL,requested);
                }
            }
        }
        if(message!=null) {
            response.setContent(message);
        }
        send(response);
    }

    @Override
    protected void setup(){
        Object [] args = getArguments();
        aids = new AID[]{
                new AID( "CarBuyer" + 1, AID.ISLOCALNAME)
        };

        cars = new ArrayList<>();
        reservation = new HashMap<>();
        for (Object arg : args) {
            Car car = Car.parseString((String) arg);
            cars.add(car);
            reservation.put(car,false);
        }
        addBehaviour(new TickerBehaviour(this,1000)
        {
            @Override
            protected void onTick() {
                ACLMessage request = receive();
                if (request!=null){
                    String message = request.getContent();
                    String [] splitMessage = message.split("::");
                    if(CarActions.fromString(splitMessage[0]) == CarActions.FIND){
                        findAction(splitMessage[1], request);
                    } else if(CarActions.fromString(splitMessage[0]) == CarActions.BUY){
                        sellAction(splitMessage[1], request);
                    } else if(CarActions.fromString(splitMessage[0]) == CarActions.CANCEL){
                        cancelAction(splitMessage[1]);
                    } else if(CarActions.fromString(splitMessage[0]) == CarActions.PAY){
                        payAction(splitMessage[1]);
                    }
                }
            }
        });
    }

    public static void main(String[] args){
        Car [] cars  = new Car [] {
               new Car ("BMW","201","wa1",10,1998,20000,5000),
               new Car ("BMW","2015","wa1",12,2000,21000,3000)
        };


        String[] bootOptions = new String[5];
        bootOptions[0] = "-gui";
        bootOptions[1] = "-local-port";
        bootOptions[2] = "1000";
        bootOptions[3] = "-agents";
        bootOptions[4] =
                "CarSeller1:agents.CarSeller(" + cars[0]+ ");"+
                "CarBuyer1:agents.CarBuyer(" + cars[0]+ ");";
        jade.Boot.main(bootOptions);


    }


}
