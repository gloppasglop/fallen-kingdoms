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
    private Map<String,Team> teams = new HashMap<>();

    private List<String> validTeamNames = Arrays.asList("RED","BLUE","GREEN","YELLOW");

    public TeamManager(FK plugin) {
        this.plugin = plugin;
    }

    public List<String> getValidTeamNames() {
        return validTeamNames;
    }

    public void addTeam(Team team) {
        plugin.getLogger().info("----:"+team.getName());
        teams.put(team.getName(),team);
        plugin.getConfigManager()
                .getConfig()
                .createSection("teams."+team.getName().toUpperCase());
        plugin.getConfigManager().saveConfig();
    }

    public void removeTeam(Team team) {
        teams.remove(team.getName());
        plugin.getConfigManager()
                .getConfig()
                .set("teams."+team.getName().toUpperCase(),null);
        plugin.getConfigManager().saveConfig();
    }

    public void addTeamCenter(Team team, Location loc) {
        team.setCenter(loc);
        String baseSection = "teams."+team.getName().toUpperCase()+".base";
        plugin.getConfigManager().getConfig().set(baseSection+".x",loc.getX());
        plugin.getConfigManager().getConfig().set(baseSection+".z",loc.getZ());
        plugin.getConfigManager().getConfig().set(baseSection+".world",loc.getWorld().getName());
        plugin.getConfigManager().saveConfig();;
    }

    public void addTeamMember(Team team, OfflinePlayer player) {
        team.getMembers().add(player);
        String teamSection = "teams."+team.getName().toUpperCase()+".members";
        List<Map<?,?>> memberList;

        memberList = plugin.getConfigManager().getConfig().getMapList(teamSection);

        Map<String,String> member = new HashMap<>();


        if (player.isOnline()) {
            member.put("uuid",player.getUniqueId().toString());
        }
        member.put("name",player.getName());
        memberList.add(member);

        plugin.getConfigManager().getConfig().set(teamSection,memberList);
        plugin.getConfigManager().saveConfig();;

    }

    public void removeTeamMember(Team team, OfflinePlayer player) {
        team.getMembers().remove(player);
    }

    public Team getTeamByName(String name) {
        return teams.get(name);
    }

    public Team getTeamByLocation(Location location) {
        return teams.entrySet().stream().filter(map -> {
            Team team = map.getValue();
            if (team.getCenter() == null) { return false;};
            if (team.getCenter().getWorld() != location.getWorld() ) {
                return false;
            }
            Double baseSize = map.getValue().getSize().doubleValue();
            Double baseX = team.getCenter().getX();
            Double baseZ = team.getCenter().getZ();

            return (Math.abs(baseX-location.getX()) <= baseSize/2 ) && ( Math.abs(baseZ-location.getZ()) <= baseSize/2 );
        }).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public Team getTeamByPlayer(Player player) {
        return teams.entrySet().stream()
                .filter(map -> map.getValue().getMembers().contains(player)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public Boolean isValidName(String name) {
        return validTeamNames.contains(name);
    }

    public Map<String,Team>  getTeams() {
        return teams;
    }


    public void loadConfig() {

        ConfigManager cm=plugin.getConfigManager();

        if ( ! cm.getConfig().contains("teams") ) {
            return;
        }


        Map<String,Team> mapTeam = new HashMap<String, Team>();

        plugin.getLogger().info("Start loadconfig: "+ plugin.getTeamManager().getTeams().size());
        cm.getConfig().getConfigurationSection("teams")
               .getValues(false)
               .forEach((k,v) -> {
                    MemorySection section = (MemorySection) v;


                    if ( isValidName(k)) {
                        Team newTeam = new Team(k);
                        plugin.getLogger().info("Name: " + k);
                        if (cm.getConfig().contains("teams."+k+".base")) {
                            Double x = section.getDouble("base.x");
                            Double z = section.getDouble("base.z");
                            int size = section.getInt("base.size");
                            plugin.getLogger().info("Team: " + k + " - " + section.get("base.x"));
                            String world = section.getString("base.world");
                            Location loc = new Location(plugin.getServer().getWorld(world), x, 0.0D, z);
                            newTeam.setCenter(loc);
                            newTeam.setSize(size);
                        }

                        List<Map<?,?>> members = section.getMapList("members");

                        for (Map<?,?> member : members) {
                            if (member.get("uuid") != null) {
                                if (Bukkit.getOfflinePlayer(UUID.fromString((String) member.get("uuid"))) != null ) {
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString( (String) member.get("uuid")));
                                    newTeam.getMembers().add(player);
                                }
                            }
                            plugin.getLogger().info("    : " + member.get("name"));
                        }

                        mapTeam.put(k,newTeam);
                    } else {
                        plugin.getLogger().warning("Invalid Team name: "+k);
                    }
        });
        teams = mapTeam;
        plugin.getLogger().info("end loadconfig: "+ plugin.getTeamManager().getTeams().size());
    }
}
