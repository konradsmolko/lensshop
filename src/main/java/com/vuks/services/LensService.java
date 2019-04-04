package com.vuks.services;

import com.vuks.model.Lens;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na książkach.
 */
@Service
public class LensService extends EntityService<Lens> {
    
    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public LensService(EntityManager em) {

        //Book.class - klasa encyjna, na której będą wykonywane operacje
        //Book::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Lens.class, Lens::getId);
    }

    /**
     * Pobranie wszystkich książek z bazy danych.
     *
     * @return lista książek
     */
    public List<Lens> findAll() {
        //pobranie listy wszystkich książek za pomocą zapytania nazwanego (ang. named query)
        //zapytanie jest zdefiniowane w klasie Book
        return em.createNamedQuery(Lens.FIND_ALL, Lens.class).getResultList();
    }
}
