package com.example.finalproject.server.controllers;

import com.example.finalproject.server.models.BoardGame;
import com.example.finalproject.server.repositories.BoardGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BoardGameController {

    @Autowired
    BoardGameRepository boardGameRepository;

    // INDEX

    @GetMapping(value = "/board-games")
    public ResponseEntity<List<BoardGame>> getAllBoardGames() {
        List<BoardGame> allGames = boardGameRepository.findAll();
        return new ResponseEntity<>(allGames, HttpStatus.OK);
    }

    // SHOW by ID

    @GetMapping(value = "/board-games/{id}")
    public ResponseEntity<Optional<BoardGame>> getBoardGameById(@PathVariable Long id) {
        Optional<BoardGame> boardGame = boardGameRepository.findById(id);
        return new ResponseEntity<>(boardGame, HttpStatus.OK);
    }

    // CREATE

    @PostMapping(value = "/board-games")
    public ResponseEntity<BoardGame> createBoardGame(@RequestBody BoardGame newBoardGame) {
        BoardGame createdBoardGame = boardGameRepository.save(newBoardGame);
        return new ResponseEntity<>(createdBoardGame, HttpStatus.CREATED);
    }

}
