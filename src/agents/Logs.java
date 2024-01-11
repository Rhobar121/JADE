package agents;

import jade.core.AID;

import java.util.ArrayList;

public class Logs {
    public static void BuyerFindLog(ArrayList<Car> items, AID agent){
        if(items != null) {
            System.out.print("Agent: " + agent.getName() + " ");
            System.out.print("looking for:\n");
            for (Car car : items) {
                System.out.print("    Brand: " + car.brand + " ");
                System.out.print(" Model: " + car.model + " ");
                System.out.print(" Year of Production: " + car.yearOfProduction + " ");
                System.out.print(" Engine: " + car.engine + " ");
                System.out.print(" Capacity: " + car.capacity + " ");
                System.out.print("\n\n");
            }
        }
    }

    public static void BuyerGetsOfferLog(Car car, AID agent, AID agent2){
        if(car!=null) {
            System.out.print("Agent: " + agent.getName() + " ");
            System.out.print("received a best sales offer from agent: " + agent2.getName());
            System.out.print(" for purchase: \n");
            System.out.print("    Brand: " + car.brand + " ");
            System.out.print(" Model: " + car.model + " ");
            System.out.print(" Year of Production: " + car.yearOfProduction + " ");
            System.out.print(" Engine: " + car.engine + " ");
            System.out.print(" for: " + car.cost + " with total price of: " + car.getTotalPrice() + " ");
            System.out.print("\n\n");
        }
    }

    public static void BuyerSendPurchaseOffer(Car car, AID agent, AID agent2){
        if(car != null) {
            System.out.print("Agent: " + agent.getName() + " ");
            System.out.print("sent a purchase offer to agent: " + agent2.getName());
            System.out.print(" for: \n");
            System.out.print("    Brand: " + car.brand + " ");
            System.out.print(" Model: " + car.model + " ");
            System.out.print(" Year of Production: " + car.yearOfProduction + " ");
            System.out.print(" Engine: " + car.engine + " ");
            System.out.print("\n\n");
        }
    }

    public static void BuyerGetApproval(Car car, AID agent, AID agent2){
        if(car != null) {
            System.out.print("Agent: " + agent.getName() + " ");
            System.out.print("start the transaction with agent: " + agent2.getName());
            System.out.print(" to buy: \n");
            System.out.print("    Brand: " + car.brand + " ");
            System.out.print(" Model: " + car.model + " ");
            System.out.print(" Year of Production: " + car.yearOfProduction + " ");
            System.out.print(" Engine: " + car.engine);
            System.out.print("\n\n");
        }
    }

    public static void BuyerBrought(Car car, AID agent, AID agent2, float currentBudget){
        if(car != null) {
            System.out.print("Agent: " + agent.getName() + " ");
            System.out.print("finished transaction with agent: " + agent2.getName());
            System.out.print(" and brought: \n");
            System.out.print("    Brand: " + car.brand + " ");
            System.out.print(" Model: " + car.model + " ");
            System.out.print(" Year of Production: " + car.yearOfProduction + " ");
            System.out.print(" Engine: " + car.engine + "\n");
            System.out.print("    Agent: "+agent.getName()+ " current budget is: " + currentBudget + " ");
            System.out.print("\n\n");
        }

    }

    public static void BuyerGetDenial(Car car, AID agent, AID agent2){
        if(car != null) {
            System.out.print("Agent: " + agent2.getName() + " refused to sell:\n");
            System.out.print("    Brand: " + car.brand + " ");
            System.out.print(" Model: " + car.model + " ");
            System.out.print(" Year of Production: " + car.yearOfProduction + " ");
            System.out.print(" Engine: " + car.engine + " ");
            System.out.print(" for: " + car.cost + " with total price of: " + car.getTotalPrice() + " \n");
            System.out.print("to: ");
            System.out.print("agent: " + agent.getName());
            System.out.print("\n\n");
        }
    }

    public static void AgentDead(AID agent){
        System.out.print("Agent: " + agent.getName() + " finished work");
        System.out.print("\n\n");
    }
}
