package com.gloppasglop.fk;

import com.gloppasglop.fk.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by christopheroux on 30/08/16.
 */
public class TeamManager {

    private FK plugin;
    private Set<Team> teams;

    private List<String> validTeamNames = Arrays.asList("RED","BLUE","GREEN","YELLOW");

    public TeamManager(FK plugin) {
        this.plugin = plugin;
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


    public void loadConfig() {

        ConfigManager cm=plugin.getConfigManager();

        if ( ! cm.getConfig().contains("teams") ) {
            return;
        }

       cm.getConfig().getConfigurationSection("teams")
               .getValues(false)
               .forEach((k,v) -> {
                    MemorySection section = (MemorySection) v;
                    Double x = section.getDouble("base.x");
                    Double z = section.getDouble("base.z");
                    int size = section.getInt("base.size");
                    plugin.getLogger().info("Team: "+k+" - " + section.get("base.x"));
                    String world = section.getString("base.world");

                    if ( isValidName(k)) {
                        Team newTeam = new Team(k);
                        newTeam.setSize(size);
                        Location loc = new Location(plugin.getServer().getWorld(world),x, 0.0D, z);
                        newTeam.setCenter(loc);

                        List<Map<?,?>> members = section.getMapList("members");

                        for (Map<?,?> member : members) {
                            if (member.get("uuid") != null) {
                                if (Bukkit.getOfflinePlayer(UUID.fromString((String) member.get("uuid"))) != null ) {
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString( (String) member.get("uuid")));
                                    newTeam.getMembers().add(player);
                                }
                            }
                            plugin.getLogger().info("Name: " + member.get("name"));
                        }

                        plugin.getLogger().info("Members: "+section.getList("members").toString());


                        addTeam(newTeam);
                    } else {
                        plugin.getLogger().warning("Invalid Team name: "+k);
                }

       });
    }
}
