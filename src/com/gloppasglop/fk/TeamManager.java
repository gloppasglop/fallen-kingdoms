package com.gloppasglop.fk;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by christopheroux on 30/08/16.
 */
public class TeamManager {

    private Set<Team> teams;

    private List<String> validTeamNames = Arrays.asList("RED","BLUE","GREEN","YELLOW");

    public TeamManager() {
        teams = new HashSet<>();
    }

    public List<String> getValidTeamNames() {
        return validTeamNames;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public Team getTeamByName(String name) {
        return teams.stream().filter(team -> team.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Team getTeamByLocation(Location location) {
        return teams.stream().filter(team -> {
            if (team.getCenter() == null) { return false;};
            if (team.getCenter().getWorld() != location.getWorld() ) {
                return false;
            }
            Double baseSize = team.getSize().doubleValue();
            Double baseX = team.getCenter().getX();
            Double baseZ = team.getCenter().getZ();

            return (Math.abs(baseX-location.getX()) <= baseSize/2 ) && ( Math.abs(baseZ-location.getZ()) <= baseSize/2 );
        }).findFirst().orElse(null);
    }

    public Team getTeamByPlayer(Player player) {
        return teams.stream().filter(team -> team.getMembers().contains(player)).findFirst().orElse(null);
    }

    public Boolean isValidName(String name) {
        return validTeamNames.contains(name);
    }

    public Set<Team> getTeams() {
        return teams;
    }
}
