package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by christopheroux on 27/08/16.
 */
public class JoinEvent implements Listener {

    private FK plugin;

    public JoinEvent(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // check game state
        // if game has started:
        //    check if player is member of a team
        //    if yes, add it to the game
        //    if no, add player in spectator mode (optional)

        Player player = event.getPlayer();

        plugin.getScoreboardHandler().sendPlayerScoreboard(player);

        if (plugin.getGameState() != FK.GameState.LOBBY) {
            if (plugin.getTeamManager().getTeamByPlayer(player) == null) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.setAllowFlight(false);
                player.setGameMode(GameMode.SURVIVAL);
            }
        } else {
            player.setFoodLevel(20);
            player.setHealth(20.0);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
        }
    }
}
