package com.vuks;

import com.vuks.model.Lens;
import com.vuks.model.Order;
import com.vuks.services.OrdersService;
import com.vuks.services.exceptions.NotEnoughItemsException;
import com.vuks.services.exceptions.OutOfStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

    @Test(expected = OutOfStockException.class)
    public void whenOrderedLensNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Lens lens = new Lens();
        lens.setAmount(0);
        order.getLenses().add(lens);
        order.getLenses().add(lens);

        Mockito.when(em.find(Lens.class, lens.getId())).thenReturn(lens);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }
    
    @Test(expected = NotEnoughItemsException.class)
    public void whenOrderedLessThan2Lenses_placeOrderThrowsNotEnItemsEx() {
        //Arrange
        Order order = new Order();
        Lens lens = new Lens();
        lens.setAmount(2);
        order.getLenses().add(lens);
        
        Mockito.when(em.find(Lens.class, lens.getId())).thenReturn(lens);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test
    public void whenOrderedDifferentLensesAvailable_placeOrderDecreasesAmountByTwo() {
        //Arrange
        Order order = new Order();
        Lens lens1 = new Lens();
        Lens lens2 = new Lens();
        lens1.setAmount(1);
        lens2.setAmount(1);
        order.getLenses().add(lens1);
        order.getLenses().add(lens2);

        Mockito.when(em.find(Lens.class, lens1.getId())).thenReturn(lens1);
        Mockito.when(em.find(Lens.class, lens2.getId())).thenReturn(lens2);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba soczewek zmniejszyła się:
        assertEquals(0, (int)lens1.getAmount());
        assertEquals(0, (int)lens2.getAmount());
        //nastąpiły dokładnie dwa wywołania em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(2)).persist(order);
    }
    
    @Test
    public void whenOrderedIdenticalLensesAvailable_placeOrderDecreasesAmountByTwo() {
        //Arrange
        Order order = new Order();
        Lens lens = new Lens();
        lens.setAmount(2);
        order.getLenses().add(lens);
        order.getLenses().add(lens);
        

        Mockito.when(em.find(Lens.class, lens.getId())).thenReturn(lens);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba soczewek zmniejszyła się:
        assertEquals(0, (int)lens.getAmount());
        //nastąpiły dokładnie dwa wywołania em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(2)).persist(order);
    }
    
    @Test
    public void whenGivenLowercaseString_toUpperReturnsUppercase() {

        //Arrange
        String lower = "abcdef";

        //Act
        String result = lower.toUpperCase();

        //Assert
        assertEquals("ABCDEF", result);
    }
}
