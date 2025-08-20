package com.ridetogether.chat_service;

import com.ridetogether.ridetogether.model.Ride;
import com.ridetogether.ridetogether.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat-history")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @GetMapping("/rides")
    public List<String> getRides() {
        return rideRepository.findAll().stream()
                .map(Ride::getName)
                .collect(Collectors.toList());
    }

    @PostMapping("/rides")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.trim().isEmpty())
            return ResponseEntity.badRequest().body("Missing chat room name");
        if (rideRepository.findByName(name.trim()).isPresent())
            return ResponseEntity.status(409).body("Room already exists");
        Ride ride = new Ride();
        ride.setName(name.trim());
        rideRepository.save(ride);
        return ResponseEntity.ok().build();
    }
}
