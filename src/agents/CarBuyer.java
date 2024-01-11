package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.glassfish.pfl.basic.logex.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CarBuyer extends Agent{
    ArrayList<Car> cars;
    Float budget;
    AID [] aids;
    boolean requestFindSend = false;
    boolean requestBuySend = false;

    int requestCounter;
    ACLMessage [] findResponses;

    HashMap<Car, Tuple> bestOffer = new HashMap<>();

    private void SendFindRequest(){
        ACLMessage request = new ACLMessage( ACLMessage.REQUEST );
        String message = MessageBuilder.responseBuilder(CarActions.FIND, cars);
        Logs.BuyerFindLog(cars, getAID());
        request.setContent(message);

        for(AID aid : aids){
            request.addReceiver(aid);
        }

        send(request);
        requestFindSend = true;
    }

    private void GetAllOffers(){
        for (int i = 0; i< findResponses.length; i++){
            ACLMessage response = blockingReceive(5000);
            if (response!=null){
                findResponses[i] = response;
            }
        }
    }
    private void FindResponseValidate(String message, ACLMessage response){
        Car [] offer = Car.splitStringAndGetCars(message,";");
        if(offer.length > 0){
            for (Car car : bestOffer.keySet()){
                for (Car car1 : offer){
                    if(car.equals(car1) && car1.getTotalPrice() < bestOffer.get(car).totalPrice){
                        bestOffer.replace(car,new Tuple(response, car1.getTotalPrice()));
                        Logs.BuyerGetsOfferLog(car1,getAID(),response.getSender());
                    }
                }
            }
        }
    }

    private void SendBuyRequest(){
        requestCounter = 0;
        for (Car car : bestOffer.keySet()){
            if(bestOffer.get(car).message != null) {
                String[] offer = bestOffer.get(car).message.getContent().split("::");
                Car requestedCar = Car.parseString(offer[1]);
                if(requestedCar!=null){
                    String message = MessageBuilder.responseBuilder(CarActions.BUY, Car.parseString(offer[1]));
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                    request.addReceiver(bestOffer.get(car).message.getSender());
                    request.setContent(message);
                    send(request);
                    Logs.BuyerSendPurchaseOffer(Car.parseString(offer[1]),getAID(),bestOffer.get(car).message.getSender());
                    requestCounter++;
                }
            }
        }
        requestBuySend = true;
    }
    @Override
    protected void setup(){
        budget = 100000f;

        Object [] args = getArguments();
        System.out.println(getAID());

        aids = new AID[]{
                new AID( "CarSeller" + 1, AID.ISLOCALNAME)
        };

        cars = new ArrayList<>();
        for (Object arg : args) {
            cars.add(Car.parseString((String) arg));
        }

        for (Car car : cars){
            bestOffer.put(car,new Tuple(null,Float.MAX_VALUE));
        }

        findResponses = new ACLMessage [aids.length];

        addBehaviour(new TickerBehaviour(this, 1000)
        {
            @Override
            protected void onTick() {
                if(!requestFindSend){
                    SendFindRequest();
                }

                if(!requestBuySend && requestFindSend){
                    GetAllOffers();
                    for (ACLMessage response : findResponses){
                        if(response!=null && response.getContent()!=null){
                            String [] splitMessage = response.getContent().split("::");
                            if(CarActions.fromString(splitMessage[0]) == CarActions.FIND){
                                FindResponseValidate(splitMessage[1], response);
                            }
                        }
                    }
                    SendBuyRequest();
                }

                if(requestBuySend){
                    for(int i=0;i<requestCounter;i++){
                        ACLMessage response = blockingReceive();
                        if(response!=null && response.getContent()!=null){
                            String [] splitMessage = response.getContent().split("::");
                            Car car = Car.parseString(splitMessage[1]);
                            if(response.getContent().contains(CarActions.APPROVE.toString())){
                               if (car != null){
                                   ACLMessage request;
                                   String responseMessage;
                                   int index = -1;
                                   for (int j = 0; j < cars.size(); j++) {
                                       if (cars.get(j).equals(car)) {
                                           index = j;
                                       }
                                       if(budget-car.getTotalPrice()>=0 && index != -1) {
                                           budget -= car.getTotalPrice();
                                           cars.remove(index);
                                           responseMessage = MessageBuilder.responseBuilder(CarActions.PAY, car);
                                       } else {
                                           responseMessage = MessageBuilder.responseBuilder(CarActions.CANCEL, car);
                                       }
                                       request = new ACLMessage(ACLMessage.CONFIRM);
                                       request.setContent(responseMessage);
                                       request.setSender(request.getSender());
                                       send(request);
                                       Logs.BuyerBrought(car,getAID(),response.getSender(),budget);
                                   }
                               }
                            }else if(response.getContent().contains((CarActions.DENIAL.toString()))){
                               Logs.BuyerGetDenial(car, getAID(), response.getSender());
                           }
                            requestFindSend = false;
                            requestBuySend = false;
                        }
                    }
                }

                if(cars.isEmpty()){
                    Logs.AgentDead(getAID());
                    doDelete();
                }
            }
        });


    }
}
