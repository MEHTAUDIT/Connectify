package com.sdp.connections_service.repository;

import com.sdp.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    // Check if a connection request exists
    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    Boolean checkConnectionRequestExists(Long senderId, Long receiverId);

    // Check if two users are already connected
    @Query("MATCH (personA:Person) -[r:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "AND personB.userId = $receiverId " +
            "RETURN COUNT(r) > 0")
    Boolean alreadyConnected(Long userId, Long receiverId);

    // Add a connection request (simulate undirected edge)
    @Query("MATCH (p1:Person), (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    // Accept a connection request (simulate undirected edge)
    @Query("MATCH (personA:Person) -[r:REQUESTED_TO]-> (personB:Person) " +
            "WHERE personA.userId = $senderId " +
            "AND personB.userId = $receiverId " +
            "DELETE r " +
            "WITH personA, personB " +
            "CREATE (personA) -[:CONNECTED_TO]-> (personB), " +
            "       (personB) -[:CONNECTED_TO]-> (personA)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    // Reject a connection request
    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);

    // Add a person node with name and userId
    @Query("CREATE (p:Person {name: $name, userId: $userId}) RETURN p")
    void addPerson(String name, Long userId);

    // Check if a user exists
    @Query("MATCH (p:Person) WHERE p.userId = $userId RETURN COUNT(p) > 0")
    Boolean checkUserExistsInGraph(Long userId);
}
