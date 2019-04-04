/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuks.controllers;

import com.vuks.model.Lens;
import com.vuks.services.LensService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 *
 * @author Vuks
 */
@RestController
@RequestMapping("/lenses")
public class LensController {
    final LensService lensService;
    
    public LensController(LensService lensService) {
        this.lensService = lensService;
    }
    
    @GetMapping
    public List<Lens> listLenses() {
        return lensService.findAll();
    }
    
    /**
     * @param lens
     * 
     * @param uriBuilder
     * 
     * @return odp. HTTP
     */
    @PostMapping
    public ResponseEntity<Void> addLens(@RequestBody Lens lens, UriComponentsBuilder uriBuilder) {

        if (lensService.find(lens.getId()) == null) {
            lensService.save(lens);

            URI location = uriBuilder.path("/lenses/{id}").buildAndExpand(lens.getId()).toUri();
            return ResponseEntity.created(location).build();

        } else {
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    /**
     * Pobieranie informacji
     *
     * Żądanie:
     * GET /lenses/{id}
     *
     * @param id identyfikator soczewki
     *
     * @return odpowiedź 200 lub odpowiedź 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lens> getLens(@PathVariable UUID id) {
        //wyszukanie książki w bazie danych
        Lens lens = lensService.find(id);

        return lens != null ? ResponseEntity.ok(lens) : ResponseEntity.notFound().build();
    }

    /**
     * Aktualizacja danych
     *
     * Żądanie:
     * PUT /lenses/{id}
     *
     * @param lens
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLens(@RequestBody Lens lens) {
        if (lensService.find(lens.getId()) != null) {
            lensService.save(lens);
            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
