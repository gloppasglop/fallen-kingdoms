package com.gloppasglop.fk;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christopheroux on 28/08/16.
 */
public class Team {

    private String name;
    private Location center;
    private org.bukkit.scoreboard.Team team;
    private Integer size=20;
    private List<OfflinePlayer> members = new ArrayList<>();
    private Location chestLocation;
    private BukkitTask effectTask;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
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

    public List<OfflinePlayer> getMembers() {
        return members;
    }

    public void setMembers(List<OfflinePlayer> members) {
        this.members = members;
    }

    public void setTeam(org.bukkit.scoreboard.Team team) {
        this.team = team;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public void setChestLocation(Location chestLocation) {
        this.chestLocation = chestLocation;
    }

    public BukkitTask getEffectTask() {
        return effectTask;
    }

    public void setEffectTask(BukkitTask effectTask) {
        this.effectTask = effectTask;
    }

}
