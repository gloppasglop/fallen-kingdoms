package com.gloppasglop.fk.commands;

import com.gloppasglop.fk.FK;
import com.gloppasglop.fk.Team;
import com.gloppasglop.fk.TeamManager;
import com.gloppasglop.fk.utils.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by christopheroux on 20/08/16.
 */
public class Fk implements TabExecutor {

    private FK plugin;

    public Fk(FK plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }

        Player player = (Player) sender;
        BukkitTask task;
        TeamManager tm = plugin.getTeamManager();
        Set<Team> teams = tm.getTeams();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                // check at least 2 teams defined
                if (teams.size() <2 ) {
                    sender.sendMessage(ChatColor.DARK_RED+ "At least 2 teams must be defined before starting.");
                    return false;
                }

                //get teams with no players
                List<String> notEnoughPlayerTeams = teams.stream()
                        .filter(team -> team.getMembers().size() == 0 )
                        .map(Team::getName)
                        .collect(Collectors.toList());

                if (notEnoughPlayerTeams.size() > 0) {
                    for (String teamname: notEnoughPlayerTeams) {
                        sender.sendMessage(ChatColor.DARK_RED+ "Not enough players in team: "+ teamname);
                    }
                    return false;
                }

                task = new Countdown(plugin, 10).runTaskTimer(plugin, 0, 20);
                return true;
            } else if (args[0].equalsIgnoreCase("stop")) {
                plugin.gametime.stop();
                Bukkit.broadcastMessage(ChatColor.GOLD + "The game is stopped!");
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }

        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pause")) {
                if (args[1].equalsIgnoreCase("on")) {
                    if (plugin.gametime.isRunning()) {
                        plugin.gametime.stop();
                        Bukkit.broadcastMessage(ChatColor.GOLD + "The game is paused!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.DARK_RED + "Game is not running");
                    return false;
                }

                if (args[1].equalsIgnoreCase("off")) {
                    if (plugin.gametime.isRunning()) {
                        sender.sendMessage(ChatColor.DARK_RED + "Game is already running");
                        return false;
                    } else {
                        plugin.gametime.restart();
                        Bukkit.broadcastMessage(ChatColor.GOLD + "The game has restarted!");
                        return true;
                    }
                }

                sender.sendMessage(ChatColor.DARK_RED + "/fk pause on|off");
                return false;
            }
            if (args[0].equalsIgnoreCase("settime")) {
                try {
                    int time = Integer.parseInt(args[1]);
                    plugin.gametime.setTime(time);
                    return true;
                } catch (NumberFormatException n) {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid Number specified!");
                    return false;
                }
            }
            if (args[0].equalsIgnoreCase("team")) {
                if (args[1].equalsIgnoreCase("list")) {
                    if (teams.size() == 0) {
                        sender.sendMessage(ChatColor.DARK_RED + "No teams defined!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "List of defined teams:");
                        for (Team team : teams) {
                            sender.sendMessage(ChatColor.DARK_RED + "    - " + team.getName());
                        }
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }
            sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
            return false;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("team")) {

                if (args[1].equalsIgnoreCase("info")) {
                    String teamname = args[2];
                    Team team = tm.getTeamByName(teamname);

                    if (team == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" does not exist!");
                        return false;
                    }

                    sender.sendMessage(ChatColor.YELLOW + "Details for team \"" + teamname + "\":");
                    sender.sendMessage(ChatColor.YELLOW + "    Members:");
                    for (Player pls : team.getMembers()) {
                        sender.sendMessage(ChatColor.YELLOW + "    - " + pls.getName());
                    }

                    if (team.getCenter() == null) {
                        sender.sendMessage(ChatColor.YELLOW + "    Base location: " + ChatColor.RED + "Not set!");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "    Base location: " + team.getCenter().getX() +
                                ",_," + team.getCenter().getX());

                    }
                    return true;
                }

                if (args[1].equalsIgnoreCase("create") ) {
                    String teamname = args[2];
                    Team team = tm.getTeamByName(teamname);

                    if (team != null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" already exists!");
                        return false;
                    }

                    if ( !tm.isValidName(teamname)) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid team name!");
                        tm.getValidTeamNames().forEach(name -> sender.sendMessage(ChatColor.DARK_RED+"    - "+name));
                        return false;
                    }

                    tm.addTeam(new Team(teamname));
                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" created.");
                    return true;

                }


                if (args[1].equalsIgnoreCase("delete")) {
                    String teamname = args[2];
                    Team team = tm.getTeamByName(teamname);
                    if (team != null ) {
                        tm.removeTeam(team);
                        sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" deleted.");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" does not exist.");
                        return false;
                    }
                }

                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }
            sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
            return false;
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("team")) {
                if (args[1].equalsIgnoreCase("add-member")) {
                    String teamname = args[2];
                    String playername = args[3];
                    // Check if team exists
                    if (tm.getTeamByName(teamname) == null ) {
                        sender.sendMessage(ChatColor.DARK_RED+"Invalid team name \""+teamname+"\"");
                        return false;
                    }

                    //get Player
                    Player member = Bukkit.getOnlinePlayers()
                            .stream()
                            .filter(p -> p.getName().equalsIgnoreCase(playername))
                            .findFirst()
                            .orElse(null);

                    if ( member == null ) {
                        sender.sendMessage(ChatColor.DARK_RED+"Player \""+ playername + "\"is not connected");
                        return false;
                    }

                    // Check if player is already member of a team
                    Team existingTeam = tm.getTeamByPlayer(member);

                    if ( existingTeam == null ) {
                        tm.getTeamByName(teamname).getMembers().add(member);
                        sender.sendMessage(ChatColor.DARK_RED+"Player \""+playername+"\" added to team \""+teamname+"\"");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED+"Player \""+playername+"\" is already member of team \""+existingTeam.getName()+"\"");
                        return false;
                    }
                }

                if (args[1].equalsIgnoreCase("remove-member"))  {
                    String teamname = args[2];
                    String playername = args[3];
                    if (tm.getTeamByName(teamname) == null ) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid team name \"" + teamname + "\"");
                       return false;
                    }

                    //get Player
                    Player member = Bukkit.getOnlinePlayers()
                            .stream()
                            .filter(p -> p.getName().equalsIgnoreCase(playername))
                            .findFirst()
                            .orElse(null);

                    if ( member == null ) {
                        sender.sendMessage(ChatColor.DARK_RED+"Player \""+ playername +"\"is not connected");
                        return false;
                    }

                    if (tm.getTeamByName(teamname).getMembers().contains(member)) {
                        tm.getTeamByName(teamname).getMembers().remove(member);
                        sender.sendMessage(ChatColor.DARK_RED+ "Player \""+playername+"\" removed from team \""+teamname+"\"");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Player \"" + playername + "\" is not member of team \"" + teamname + "\"");
                        return false;
                    }
                }

                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }

            sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
            return false;
        }

        if (args.length == 5 ) {
            if ( args[0].equalsIgnoreCase("team") && args[1].equalsIgnoreCase("set-base") ) {
                sender.sendMessage(ChatColor.DARK_RED + "FIVE!");
                String teamname = args[2];
                Integer x;
                Integer z;
                if (tm.getTeamByName(teamname) == null ) {
                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" does not exist.");
                    return false;
                }

                try {
                    x = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED+ "\""+ args[3] +"\" is not a valid Integer!");
                    return false;
                }

                try {
                    z = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED+ "\""+ args[4] +"\" is not a valid Integer!");
                    return false;
                }

                Location loc = new Location(player.getWorld(), x,0,z);
                tm.getTeamByName(teamname).setCenter(loc);
                sender.sendMessage(ChatColor.DARK_RED+"Base set for team \""+teamname+"\"");
                return true;

            }

            sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
            return false;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
        return false;
    }

    private List<String> complete(String search, List<String> possibilities) {
        return possibilities.stream().filter(str -> str.toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());

    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completion = new ArrayList<>();
        List<String> possibilities ;

        TeamManager tm = plugin.getTeamManager();

        if (args.length == 1) {
            possibilities = Arrays.asList("start", "stop", "pause", "team","settime");
            return complete(args[0],possibilities);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pause")) {
                possibilities = Arrays.asList("on", "off");
                return complete(args[1], possibilities);
            }

           if (args[0].equalsIgnoreCase("team")) {
               return complete(args[1],Arrays.asList("create","delete","list","info","set-base",
                                                     "add-member","remove-member"));
           }

        }

       if (args.length == 3 ) {
            if (args[0].equalsIgnoreCase("team") ) {
                List<String> validNames = new ArrayList<>();
                if ( args[1].equalsIgnoreCase("create") ) {
                    validNames=tm.getValidTeamNames();
                }
                if ( args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("add-member")
                        || args[1].equalsIgnoreCase("remove-member")
                        || args[1].equalsIgnoreCase("info")
                        || args[1].equalsIgnoreCase("set-base")) {
                    validNames.addAll(tm.getTeams().stream().map(Team::getName).collect(Collectors.toList()));
                }
                return complete(args[2],validNames);
            }
        }

       if (args.length == 4 ) {
           if ( (args[0] + " " + args[1]).equalsIgnoreCase("team add-member") ||
                   (args[0] + " " + args[1]).equalsIgnoreCase("team remove-member") ) {
               completion.addAll(Bukkit.getOnlinePlayers().stream().map((Function<Player, String>) HumanEntity::getName).collect(Collectors.toList()));
               return completion;
           }
       }


        completion.add("");
        return completion;
    }
}
