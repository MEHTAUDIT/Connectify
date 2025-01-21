package com.sdp.connections_service.controller;

import com.sdp.connections_service.entity.Person;
import com.sdp.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

        log.info("Getting first degree connections for user with id: {}", userId);
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections(userId));
    }

}
