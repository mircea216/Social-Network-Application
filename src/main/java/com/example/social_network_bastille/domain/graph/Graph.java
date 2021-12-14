package com.example.social_network_bastille.domain.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph {
    private int numberOfVertices;
    private final Map<Integer, HashSet<Integer>> graphRepresentation;
    private final Map<Integer, HashSet<Integer>> communitiesOfFriends;
    private static Map<Integer, Integer> visited;

    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        visited = new HashMap<>();
        this.communitiesOfFriends = new HashMap<>();
        this.graphRepresentation = new HashMap<>();
        for (int i = 1; i < numberOfVertices; i++) {
            graphRepresentation.putIfAbsent(i, new HashSet<>());
            visited.put(i, 0);
        }
    }

    public void addEdge(int firstVertex, int secondVertex) {
        graphRepresentation.putIfAbsent(firstVertex, new HashSet<>());
        graphRepresentation.putIfAbsent(secondVertex, new HashSet<>());
        graphRepresentation.get(firstVertex).add(secondVertex);
        graphRepresentation.get(secondVertex).add(firstVertex);
        visited.put(firstVertex, 0);
        visited.put(secondVertex, 0);
    }

    private void DepthFirstTraversal(int vertex, int counterForCommunities) {
        visited.put(vertex, 1);
        for (Integer child : graphRepresentation.get(vertex)) {
            if (visited.get(child) == 0) {
                communitiesOfFriends.get(counterForCommunities).add(child);
                DepthFirstTraversal(child, counterForCommunities);
            }
        }
    }

    public int countTheNumberOfCommunitiesOfFriends() {
        int numberOfCommunities = 0;
        for (Integer vertex : visited.keySet()) {
            if (visited.get(vertex) == 0) {
                communitiesOfFriends.putIfAbsent(numberOfCommunities, new HashSet<>());
                communitiesOfFriends.get(numberOfCommunities).add(vertex);
                DepthFirstTraversal(vertex, numberOfCommunities);
                numberOfCommunities++;
            }
        }
        return numberOfCommunities;
    }

    public HashSet<Integer> getTheMostNumerousCommunityOfFriends() {
        countTheNumberOfCommunitiesOfFriends();
        int maximumNumberOfUsers = 0;
        int keyUser = 0;
        for (Integer vertex : communitiesOfFriends.keySet()) {
            if (communitiesOfFriends.get(vertex).size() > maximumNumberOfUsers) {
                maximumNumberOfUsers = communitiesOfFriends.get(vertex).size();
                keyUser = vertex;
            }
        }
        return communitiesOfFriends.get(keyUser);
    }
}
