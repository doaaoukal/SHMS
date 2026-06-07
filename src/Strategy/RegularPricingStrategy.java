/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Strategy;
/**
 *
 * @author Doaa Oukal ID: 220230438
 */
import FactoryMethod.Room;

public class RegularPricingStrategy implements BillingStrategy {

    @Override
    public double calculateTotal(Room room, int nights) {
        return room.getPrice() * nights;
    }

    @Override
    public String getStrategyName() {
        return "Regular Pricing";
    }
}