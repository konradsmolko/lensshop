/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vuks.model;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 *
 * @author Vuks
 */
@Entity
@EqualsAndHashCode(of = "id")
@NamedQueries(value = {
        @NamedQuery(name = Lens.FIND_ALL, query = "SELECT l FROM Lens l")
})
public class Lens {
    public static final String FIND_ALL = "Lens.FIND_ALL";

    @Getter
    @Id
    UUID id = UUID.randomUUID();
    
    @Getter
    @Setter
    Double dioptria;
    
    @Getter
    @Setter
    Double cena;
    
    @Getter
    @Setter
    Integer amount;
}
/**
 * {
	"id": "15877953-84b7-4c41-96fb-e3b00c813f9d",
	"title": "Soczewka Endera",
	"amount": "2",
	"moc": "21.0",
	"cena": "99.9",
	"producent": "Sochevky Enterprise",
	"data": "20.03.1998" 
}
 */