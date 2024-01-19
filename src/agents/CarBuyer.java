package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class CarBuyer extends Agent{
    ArrayList<Car> cars;
    int budget;
    AID [] aids;
    boolean requestFindSend = false;
    boolean requestBuySend = false;
    int requestCounter;
    ACLMessage [] findResponses;
    HashMap<Car, Tuple> bestOffer = new HashMap<>();

    private void sendFindRequest(){
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

    private void getAllOffers(){
        for (int i = 0; i< findResponses.length; i++){
            ACLMessage response = blockingReceive(5000);
            if (response!=null){
                findResponses[i] = response;
            }
        }
    }
    private void findResponseValidate(String message, ACLMessage response){
        Car [] offer = Car.splitString(message,";");
        if(offer.length > 0){
            for (Car car : bestOffer.keySet()){
                for (Car car1 : offer){
                    if(car.equals(car1) && car1.getTotalPrice() < bestOffer.get(car).totalPrice
                            && car1.getTotalPrice()<budget){
                        bestOffer.replace(car,new Tuple(response, car1.getTotalPrice()));
                        Logs.BuyerGetsOfferLog(car1,getAID(),response.getSender());
                    }
                }
            }
        }
    }

    private void sendBuyRequest(){
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
        budget = 1000000;

        Object [] args = getArguments();

        aids = new AID[]{
                new AID( "CarSeller" + 1, AID.ISLOCALNAME),
                new AID( "CarSeller" + 2, AID.ISLOCALNAME),
                new AID( "CarSeller" + 3, AID.ISLOCALNAME),
                new AID( "CarSeller" + 4, AID.ISLOCALNAME),
                new AID( "CarSeller" + 5, AID.ISLOCALNAME),
                new AID( "CarSeller" + 6, AID.ISLOCALNAME),
                new AID( "CarSeller" + 7, AID.ISLOCALNAME),
                new AID( "CarSeller" + 8, AID.ISLOCALNAME),
                new AID( "CarSeller" + 9, AID.ISLOCALNAME),
                new AID( "CarSeller" + 10, AID.ISLOCALNAME)
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
                    sendFindRequest();
                }

                if(!requestBuySend && requestFindSend){
                    getAllOffers();
                    for (ACLMessage response : findResponses){
                        if(response!=null && response.getContent()!=null){
                            String [] splitMessage = response.getContent().split("::");
                            if(CarActions.fromString(splitMessage[0]) == CarActions.FIND){
                                findResponseValidate(splitMessage[1], response);
                            }
                        }
                    }
                    sendBuyRequest();
                }

                if(requestBuySend){
                    for(int i=0;i<requestCounter;i++){
                        ACLMessage response = blockingReceive();
                        if(response!=null && response.getContent()!=null){
                            ACLMessage request;
                            String responseMessage;
                            String [] splitMessage = response.getContent().split("::");
                            Car car = Car.parseString(splitMessage[1]);
                            if(response.getContent().contains(CarActions.APPROVE.toString()) && !cars.isEmpty()){
                                if (car != null){
                                    int index = -1;
                                    for (int j = 0; j < cars.size(); j++) {
                                        if (cars.get(j).equals(car)) {
                                            index = j;
                                        }
                                        if(budget-car.getTotalPrice()>=0 && index != -1) {
                                            budget -= car.getTotalPrice();
                                            cars.remove(index);
                                            responseMessage = MessageBuilder.responseBuilder(CarActions.PAY, car);
                                            request = new ACLMessage(ACLMessage.CONFIRM);
                                            request.setContent(responseMessage);
                                            request.addReceiver(response.getSender());
                                            send(request);
                                            Logs.BuyerBrought(car,getAID(),response.getSender(),budget);
                                        } else if(budget-car.getTotalPrice()<0 || index == -1){
                                            responseMessage = MessageBuilder.responseBuilder(CarActions.CANCEL, car);
                                            request = new ACLMessage(ACLMessage.CANCEL);
                                            request.setContent(responseMessage);
                                            request.addReceiver(response.getSender());
                                            send(request);
                                        }
                                    }
                                }
                            }else if(response.getContent().contains((CarActions.DENIAL.toString()))){
                                Logs.BuyerGetDenial(car, getAID(), response.getSender());
                            } else if(response.getContent().contains((CarActions.APPROVE.toString())) && cars.isEmpty()){
                                responseMessage = MessageBuilder.responseBuilder(CarActions.CANCEL, car);
                                request = new ACLMessage(ACLMessage.CANCEL);
                                request.setContent(responseMessage);
                                request.addReceiver(response.getSender());
                                send(request);
                            }
                        }
                    }
                    if(budget>0 && !cars.isEmpty()){
                        requestFindSend = false;
                        requestBuySend = false;
                        for (Car car : cars){
                            bestOffer.put(car,new Tuple(null,Float.MAX_VALUE));
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

