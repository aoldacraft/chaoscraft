package org.sigee.chaoscraft;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.sigee.chaoscraft.events.JoinQuitHandler;
import org.sigee.chaoscraft.api.core.SetGameZone;

public class EventsListener implements Listener {

    private JoinQuitHandler joinQuitHandler = new JoinQuitHandler();

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        joinQuitHandler.joinPlayer(event);
    }

    @EventHandler
    public void leavePlayer(PlayerQuitEvent event) {
        joinQuitHandler.quitPlayer(event);
    }


    @EventHandler
    public void interactWorldSetter(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp())
            return;
        SetGameZone.getInstance().updateGameWorldPos(event);
    }
}
