package agents;

import jade.core.AID;
import java.util.ArrayList;

public class Logs {
    public static void BuyerFindLog(ArrayList<Car> items, AID agent){
        if(items != null) {
            StringBuilder str = new StringBuilder();
            for (Car car : items) {
                str
                        .append("    Brand: ")
                        .append(car.brand)
                        .append("  Model: ")
                        .append(car.model)
                        .append("  Body: ")
                        .append(car.body)
                        .append("  Year of Production: ")
                        .append(car.yearOfProduction)
                        .append("  Engine: ")
                        .append(car.engine)
                        .append("  Capacity: ")
                        .append(car.capacity)
                        .append("\n\n");
            }
            System.out.print("Agent: " + agent.getName() + " " + "looking for:\n" + str);
        }
    }

    public static void BuyerGetsOfferLog(Car car, AID agent, AID agent2){
        if(car!=null) {
            String str = "Agent: " +
                    agent.getName() +
                    " received a best sales offer from agent: " +
                    agent2.getName() +
                    " for purchase: \n" +
                    "    Brand: " +
                    car.brand +
                    "  Model: " +
                    car.model +
                    "  Body: "
                    + car.body +
                    "  Year of Production: " +
                    car.yearOfProduction +
                    "  Engine: " +
                    car.engine +
                    "  for: " +
                    car.cost +
                    " with total price of: " +
                    car.getTotalPrice() +
                    "\n\n";
            System.out.print(str);
        }
    }

    public static void BuyerSendPurchaseOffer(Car car, AID agent, AID agent2){
        if(car != null) {
            String str = "Agent: "
                    + agent.getName()
                    + " sent a purchase offer to agent: "
                    + agent2.getName()
                    + " for: \n"
                    + "    Brand: "
                    + car.brand
                    + "  Model: "
                    + car.model
                    + "  Body: "
                    + car.body
                    + "  Year of Production: "
                    + car.yearOfProduction
                    + "  Engine: "
                    + car.engine
                    + "\n\n";
            System.out.print(str);
        }
    }

    public static void SellerSendApproval(Car car, AID agent, AID agent2){
        if(car != null) {
            String str = "Agent: "
                    + agent.getName()
                    + " start the transaction with agent: "
                    + agent2.getName()
                    + " to sell: \n    Brand: "
                    + car.brand
                    + "  Model: "
                    + car.model
                    + "  Body: "
                    + car.body
                    + "  Year of Production: "
                    + car.yearOfProduction
                    + "  Engine: "
                    + car.engine
                    + "\n\n";
            System.out.print(str);
        }
    }

    public static void BuyerBrought(Car car, AID agent, AID agent2, int currentBudget){
        if(car != null) {
            String str = "Agent: "
                    + agent.getName()
                    + " finished transaction with agent: "
                    + agent2.getName()
                    + " and brought: \n Brand: "
                    + car.brand
                    + "  Model: "
                    + car.model
                    + "  Body: "
                    + car.body
                    + " Year of Production: "
                    + car.yearOfProduction
                    + " Engine: "
                    + car.engine
                    + "\n    Agent: "
                    +agent.getName()
                    + " current budget is: "
                    + currentBudget
                    + "\n\n";
            System.out.print(str);
        }
    }

    public static void BuyerGetDenial(Car car, AID agent, AID agent2){
        if(car != null) {
            String str = "Agent: "
                    + agent2.getName()
                    + " refused to sell:\n    Brand: "
                    + car.brand
                    + "  Model: "
                    + car.model
                    + "  Body: "
                    + car.body
                    + "  Year of Production: "
                    + car.yearOfProduction
                    + "  Engine: "
                    + car.engine
                    + " for: "
                    + car.cost
                    + " with total price of: "
                    + car.getTotalPrice()
                    + " \nto: agent: "
                    + agent.getName()
                    + "\n\n";
            System.out.print(str);
        }
    }

    public static void AgentDead(AID agent){
        System.out.print("Agent: " + agent.getName() + " finished work"+ "\n\n");
    }

    public static void SellerSendOffer(AID agent, AID agent2){
        System.out.print("Agent: " + agent.getName() + " sent an offer to "+agent2.getName() + "\n\n");
    }

    public static void TransactionCancel(Car car, AID agent, AID agent2){
        String str = "Agent: "
                + agent2.getName()
                + " canceled transactions with:"
                + agent.getName()
                + "\nThe reservation for:\n"
                +"    Brand: "
                + car.brand
                + "  Model: "
                + car.model
                + "  Body: "
                + car.body
                + "  Year of Production: "
                + car.yearOfProduction
                + "  Engine: "
                + car.engine
                + " for: "
                + car.cost
                + " with total price of: "
                + car.getTotalPrice()
                + " has been cancelled \n\n";
        System.out.print(str);
    }

    public static void CarSold(Car car, AID agent){
        String str = "Agent: "
                + agent.getName()
                +" sold:\n"
                +"    Brand: "
                + car.brand
                + "  Model: "
                + car.model
                + "  Body: "
                + car.body
                + "  Year of Production: "
                + car.yearOfProduction
                + "  Engine: "
                + car.engine
                + " for: "
                + car.cost
                + " with total price of: "
                + car.getTotalPrice()
                +" \n\n";
        System.out.print(str);
    }
}
