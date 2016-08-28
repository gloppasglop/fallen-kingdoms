package com.gloppasglop.fk.commands;

import com.gloppasglop.fk.Team;
import com.gloppasglop.fk.utils.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gloppasglop.fk.Main.*;

/**
 * Created by christopheroux on 20/08/16.
 */
public class Fk implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }

        Player player = (Player) sender;
        Plugin plugin = getInstance();
        BukkitTask task;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                // check at least 2 teams defined
                Integer enabledTeamCount=0;
                for (Team team : getTeams()) {
                    if (team.isEnabled()) enabledTeamCount++;
                }
                if (enabledTeamCount <2 ) {
                    sender.sendMessage(ChatColor.DARK_RED+ "At least 2 teams must be defined before starting.");
                    return false;
                }
                // check at least one player in each defined teams
                Boolean notEnoughPlayers = false;
                for (Team team : getTeams()) {
                    if (team.isEnabled()) {
                        if (team.getMembers() == null ) {
                            notEnoughPlayers = true;
                            sender.sendMessage(ChatColor.DARK_RED+ "Not enough players in team: "+ team.getTeam().getName());
                        } else if (team.getMembers().size() == 0 ) {
                            notEnoughPlayers = true;
                            sender.sendMessage(ChatColor.DARK_RED+ "Not enough players in team: "+ team.getTeam().getName());
                        }
                    }
                }

                if (notEnoughPlayers) {
                    return false;
                }

                task = new Countdown(plugin, 10).runTaskTimer(plugin, 0, 20);
            } else if (args[0].equalsIgnoreCase("stop")) {
                gametime.stop();
                Bukkit.broadcastMessage(ChatColor.GOLD + "The game is stopped!");
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }

        } else {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("pause")) {
                    if (args[1].equalsIgnoreCase("on")) {
                        if (gametime.isRunning()) {
                            gametime.stop();
                            Bukkit.broadcastMessage(ChatColor.GOLD + "The game is paused!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "Game is not running");
                            return false;
                        }

                    } else if (args[1].equalsIgnoreCase("off")) {
                        if (gametime.isRunning()) {
                            sender.sendMessage(ChatColor.DARK_RED + "Game is already running");
                        } else {
                            gametime.restart();
                            Bukkit.broadcastMessage(ChatColor.GOLD + "The game has restarted!");
                        }

                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "/fk pause on|off");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("settime")) {
                    try {
                        int time = Integer.parseInt(args[1]);
                        gametime.setTime(time);
                        return true;
                    } catch (NumberFormatException n) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid Number specified!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("team")) {
                    List<String> teams = new ArrayList<>();
                    if (args[1].equalsIgnoreCase("list")) {
                        for (Team team : getTeams()) {
                            if (team.isEnabled()) teams.add(team.getTeam().getName().toUpperCase());
                        }
                        if (teams.size() == 0) {
                            sender.sendMessage(ChatColor.DARK_RED + "No teams defined!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "List of defined teams:");
                            for (String teamname : teams) {
                                sender.sendMessage(ChatColor.DARK_RED + "    - " + teamname);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                        return false;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("team")) {
                    if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("delete")) {
                        String teamname = args[2];
                        Team team = getTeam(teamname);
                        if (team == null) {
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid team name!");
                            List<String> validNames = new ArrayList<>();
                            for (Team tmpteam : getTeams()) {
                                validNames.add(tmpteam.getTeam().getName());
                            }
                            sender.sendMessage(ChatColor.DARK_RED + "Valid team names are: " + String.join(",", validNames));
                            return false;
                        } else {
                            if (args[1].equalsIgnoreCase("create")) {
                                if (team.isEnabled()) {
                                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" already exists!");
                                    return false;
                                } else {
                                    team.enable();
                                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" created.");
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("delete")) {
                                if (team.isEnabled()) {
                                    team.disable();
                                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" deleted.");
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.DARK_RED + "Team \"" + teamname + "\" does not exists.");
                                    return false;
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                        return false;
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("team")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        String teamname = args[2];
                        String playername = args[3];
                        if (getTeam(teamname) == null ) {
                            sender.sendMessage(ChatColor.DARK_RED+"Invalid team name \""+teamname+"\"");
                            return false;
                        } else {
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid command!");
                return false;
            }
        }
        return false;
    }

    private List<String> complete(String search, List<String> possibilities) {
        List<String> completion = new ArrayList<>();
        for (String str : possibilities) {
            if (str.toLowerCase().startsWith(search.toLowerCase()) ) {
                completion.add(str);
            }
        }
        return completion;

    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completion = new ArrayList<>();
        List<String> possibilities ;
        if (args.length == 1) {
            possibilities = Arrays.asList("start", "stop", "pause", "team","settime");
            return complete(args[0],possibilities);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pause")) {
                possibilities = Arrays.asList("on","off");
                return complete(args[1],possibilities);
            } else if (args[0].equalsIgnoreCase("team")) {
               return complete(args[1],Arrays.asList("create","delete","list","add"));
            }
        } else if (args.length == 3 ) {
            if (args[0].equalsIgnoreCase("team") ) {
                List<String> validNames = new ArrayList<>();
                if ( args[1].equalsIgnoreCase("create") ) {
                    for (Team tmpteam : getTeams()) {
                        if ( ! tmpteam.isEnabled()) {
                            validNames.add(tmpteam.getTeam().getName());
                        }
                    }
                }
                if ( args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("add") ) {
                    for (Team tmpteam : getTeams()) {
                        if ( tmpteam.isEnabled()) {
                            validNames.add(tmpteam.getTeam().getName());
                        }
                    }
                }
                return complete(args[2],validNames);
            }
        } else if (args.length == 4 ) {
           if ((args[0] + " " + args[1]).equalsIgnoreCase("team add")) {
               for (Player player: Bukkit.getOnlinePlayers()) {
                   completion.add(player.getName());
               }
           }
        }

        completion.add("");
        return completion;
    }
}
