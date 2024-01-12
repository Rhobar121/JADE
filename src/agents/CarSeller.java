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
        Logs.SellerSendOffer(getAID(), receivedMessage.getSender());
        send(response);
    }

    private void cancelAction(String request, ACLMessage message){
        Car requested = Car.parseString(request);
        for(Car car : cars){
            if(car.equals(requested)){
                reservation.replace(requested,false);
            }
        }
        if(requested!=null) {
            Logs.TransactionCancel(requested, getAID(), message.getSender());
        }
    }

    private void payAction(String request){
        Car requested = Car.parseString(request);
        Car deleted = null;
        for(Car car : cars){
            if(car.equals(requested)){
                deleted = car;
            }
        }
        if(deleted!=null) {
            reservation.remove(deleted);
            cars.remove(deleted);
            Logs.CarSold(deleted, getAID());
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
                    Logs.SellerSendApproval(car,getAID(),receivedMessage.getSender());
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
                new AID( "CarBuyer" + 1, AID.ISLOCALNAME),
                new AID( "CarBuyer" + 2, AID.ISLOCALNAME),
                new AID( "CarBuyer" + 3, AID.ISLOCALNAME)
        };

        cars = new ArrayList<>();
        reservation = new HashMap<>();
        for (Object arg : args) {
            Car car = Car.parseString((String) arg);
            cars.add(car);
            reservation.put(car,false);
        }
        addBehaviour(new TickerBehaviour(this,500)
        {
            @Override
            protected void onTick() {
                ACLMessage request = blockingReceive();
                if (request!=null){
                    String message = request.getContent();
                    String [] splitMessage = message.split("::");
                    if(CarActions.fromString(splitMessage[0]) == CarActions.FIND) {
                        findAction(splitMessage[1], request);
                    }else if(CarActions.fromString(splitMessage[0]) == CarActions.PAY){
                        payAction(splitMessage[1]);
                    } else if(CarActions.fromString(splitMessage[0]) == CarActions.BUY){
                        sellAction(splitMessage[1], request);
                    } else if(CarActions.fromString(splitMessage[0]) == CarActions.CANCEL){
                        cancelAction(splitMessage[1], request);
                    }
                }
                if(cars.isEmpty()){
                    Logs.AgentDead(getAID());
                   // doDelete();
                }
            }
        });
    }

    public static void main(String[] args){
        Car[] carToBuy = new Car[] {
                new Car ("BMW","201","wa1",10,1998),
                new Car ("AUDI","V2","w3a1",10,2010),
                new Car ("DD","201","wa1",10,1998),
                new Car ("W!","201","wa1",10,1998),
        };
        Car [] cars  = new Car [] {
                new Car ("BMW","201","wa1",10,1998,20000,2500),
                new Car ("BMW","201","wa1",10,1998,30000,5000),
                new Car ("BMW","201","wa1",10,1998,30000,500),
                new Car ("BMW","201","wa1",10,1998,40000,2500),
                new Car ("BMW","201","wa1",10,1998,20000,5000),
                new Car ("BMW","201","wa1",10,1998,50000,500),
                new Car ("AUDI","V2","w3a1",10,2010,10000,500),
                new Car ("AUDI","V2","w3a1",10,2010,30000,500),
                new Car ("AUDI","V2","w3a1",10,2010,50000,500),
                new Car ("AUDI","V2","w3a1",10,2010,10000,500),
                new Car ("AUDI","V2","w3a1",10,2010,20000,500),
                new Car ("DD","201","wa1",10,1998,50000,500),
                new Car ("DD","201","wa1",10,1998,60000,5000),
                new Car ("DD","201","wa1",10,1998,40000,2500),
                new Car ("DD","201","wa1",10,1998,10000,500),
                new Car ("W!","201","wa1",10,1998,30000,2500),
                new Car ("W!","201","wa1",10,1998,50000,500),
                new Car ("W!","201","wa1",10,1998,20000,2500),
                new Car ("W!","201","wa1",10,1998,50000,3500),
                new Car ("W!","201","wa1",10,1998,10000,500),
                new Car ("BMW","201","wa1",10,1998,5000,1000),
        };


        String[] bootOptions = new String[5];
        bootOptions[0] = "-gui";
        bootOptions[1] = "-local-port";
        bootOptions[2] = "1000";
        bootOptions[3] = "-agents";
        bootOptions[4] =
                "CarSeller1:agents.CarSeller("
                        + cars[0]+ " " + cars[1] + " "
                        + cars[2] + " " + cars[3] + " "
                        + cars[4] + " " + cars[5] + " "
                        + cars[6] + " " + cars[7]+");"+
                "CarSeller2:agents.CarSeller("
                        + cars[8]+ " " + cars[9] + " "
                        + cars[10] + " " + cars[11] + " "
                        + cars[12] + " " + cars[13] + " "
                        + cars[14] + " " + cars[15]+");"+
                "CarSeller3:agents.CarSeller("
                        + cars[0]+ " " + cars[15] + " "
                        + cars[1] + " " + cars[14] + " "
                        + cars[2] + " " + cars[13] + " "
                        + cars[3] + " " + cars[19]+");"+
                "CarSeller4:agents.CarSeller("
                        + cars[13]+ " " + cars[12] + " "
                        + cars[14] + " " + cars[15] + " "
                        + cars[17] + " " + cars[16] + " "
                        + cars[18] + " " + cars[19]+");"+
               "CarSeller5:agents.CarSeller("
                        + cars[0]+ " " + cars[10] + " "
                        + cars[1] + " " + cars[11] + " "
                        + cars[2] + " " + cars[12] + " "
                        + cars[3] + " " + cars[13]+");"+
               "CarSeller6:agents.CarSeller("
                        + cars[19]+ " " + cars[15] + " "
                        + cars[18] + " " + cars[14] + " "
                        + cars[17] + " " + cars[16] + " "
                        + cars[16] + " " + cars[0]+");"+
               "CarSeller7:agents.CarSeller("
                        + cars[1]+ " " + cars[5] + " "
                        + cars[15] + " " + cars[9] + " "
                        + cars[15] + " " + cars[10] + " "
                        + cars[14] + " " + cars[12]+");"+
               "CarSeller8:agents.CarSeller("
                        + cars[13]+ " " + cars[12] + " "
                        + cars[14] + " " + cars[15] + " "
                        + cars[17] + " " + cars[16] + " "
                        + cars[18] + " " + cars[19]+");"+
               "CarSeller9:agents.CarSeller("
                        + cars[0]+ " " + cars[1] + " "
                        + cars[14] + " " + cars[2] + " "
                        + cars[5] + " " + cars[4] + " "
                        + cars[18] + " " + cars[20]+");"+
               "CarSeller10:agents.CarSeller("
                        + cars[3]+ " " + cars[1] + " "
                        + cars[4] + " " + cars[2] + " "
                        + cars[5] + " " + cars[4] + " "
                        + cars[19] + " " + cars[20]+");"+
               "CarBuyer1:agents.CarBuyer(" + carToBuy[0]+ " " + carToBuy[1] + " " + carToBuy[2]+");"+
               "CarBuyer2:agents.CarBuyer(" + carToBuy[0]+ " " + carToBuy[2] + " " + carToBuy[3]+");"+
               "CarBuyer3:agents.CarBuyer(" + carToBuy[3]+ " " + carToBuy[2] + " " + carToBuy[1]+");";
        jade.Boot.main(bootOptions);


    }


}
