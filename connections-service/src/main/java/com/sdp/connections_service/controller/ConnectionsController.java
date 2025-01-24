package com.sdp.connections_service.controller;

import com.sdp.connections_service.auth.UserContextHolder;
import com.sdp.connections_service.entity.Person;
import com.sdp.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId) {

//        Long userId= UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for user with id: {}", userId);
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections(userId));
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
    }
}
