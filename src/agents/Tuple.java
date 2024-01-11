package agents;

import jade.lang.acl.ACLMessage;

public class Tuple {
    ACLMessage message;
    float totalPrice;

    public Tuple(ACLMessage message, float totalPrice){
        this.message = message;
        this.totalPrice = totalPrice;
    }
}
