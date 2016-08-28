package com.gloppasglop.fk;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by christopheroux on 28/08/16.
 */
public class Team {

    private Location center;
    private org.bukkit.scoreboard.Team team;
    private int size=10;
    private List<Player> members;
    private boolean isEnabled;

    public Team(Location center, org.bukkit.scoreboard.Team team) {
        this.center = center;
        this.team = team;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public void setTeam(org.bukkit.scoreboard.Team team) {
        this.team = team;
    }

    public void enable() {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }
}
