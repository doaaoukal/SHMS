/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Strategy;
/**
 *
 * @author Doaa Oukal ID: 220230438
 */
import FactoryMethod.Room;

public interface BillingStrategy {
    double calculateTotal(Room room, int nights);
    String getStrategyName();
}