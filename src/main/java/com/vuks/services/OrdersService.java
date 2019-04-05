/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuks.services;

import com.vuks.model.Lens;
import com.vuks.model.Order;
import com.vuks.services.exceptions.NotEnoughItemsException;
import com.vuks.services.exceptions.OrderPriceNotHighEnoughException;
import com.vuks.services.exceptions.OutOfStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
/**
 * Komponent (serwis) biznesowy do realizacji operacji na zamówieniach.
 */
@Service
public class OrdersService extends EntityService<Order> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public OrdersService(EntityManager em) {

        //Order.class - klasa encyjna, na której będą wykonywane operacje
        //Order::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Order.class, Order::getId);
    }

    /**
     * Pobranie wszystkich zamówień z bazy danych.
     *
     * @return lista zamówień
     */
    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    /**
     * Złożenie zamówienia w sklepie.
     * <p>
     * Zamówienie jest akceptowane, jeśli wszystkie objęte nim produkty są dostępne (przynajmniej 1 sztuka). W wyniku
     * złożenia zamówienia liczba dostępnych sztuk produktów jest zmniejszana o jeden. Metoda działa w sposób
     * transakcyjny - zamówienie jest albo akceptowane w całości albo odrzucane w całości. W razie braku produktu
     * wyrzucany jest wyjątek OutOfStockException.
     *
     * @param order zamówienie do przetworzenia
     */
    @Transactional
    public void placeOrder(Order order) {
        List<Lens> llst = order.getLenses();
        if (llst.size() < 2)
            //wyjątek z hierarchii RuntimeException powoduje wycofanie transakcji (rollback)
            throw new NotEnoughItemsException();
        
        int kwota = 0;
        for (Lens lensStub : llst) {
            kwota += lensStub.getCena();
        }
        if (kwota < 10.0)
            throw new OrderPriceNotHighEnoughException();
        
        for (Lens lensStub : llst) {
            Lens lens = em.find(Lens.class, lensStub.getId());

            if (lens.getAmount() < 1) {
                //wyjątek z hierarchii RuntimeException powoduje wycofanie transakcji (rollback)
                throw new OutOfStockException();
            } else {
                int newAmount = lens.getAmount() - 1;
                lens.setAmount(newAmount);
            }
        }

        //jeśli wcześniej nie został wyrzucony wyjątek, zamówienie jest zapisywane w bazie danych
        save(order);
    }
}