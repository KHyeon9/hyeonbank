package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Cards;
import com.springsecurity.hyeonbank.repository.CardsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {

    private CardsRepository cardsRepository;

    public CardsController(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @GetMapping("myCards")
    public List<Cards> getCardDetails(@RequestParam int customerId) {
        List<Cards> cards = cardsRepository.findByCustomerId(customerId);

        return cards;
    }
}
