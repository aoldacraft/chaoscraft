package org.sigee.chaoscraft.api.core;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.sigee.chaoscraft.ChaosCraft;
import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.api.entities.enums.GameState;
import org.sigee.chaoscraft.utils.ConfigFileUtil;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : WorldSetter
 * @since : 2023/04/15
 */
public class SetGameZone {
    private static final class InstanceHolder {
        private static final SetGameZone instance = new SetGameZone();
    }
    public static SetGameZone getInstance() {
        return SetGameZone.InstanceHolder.instance;
    }

    private HashSet<Player> setters = new HashSet<>();
    private HashMap<Player, ItemStack[]> tmpSaveInventory = new HashMap<>();
    private final Inventory emptyInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
    private ItemStack settingItem = new ItemStack(Material.BLAZE_ROD, 1);
    private boolean checkPlayerIsSetter(Player player){
        return setters.contains(player);
    }


    public void onCommands(Player op, String args[]) {
        if(args.length < 2 || args[1].equalsIgnoreCase("help")) {
            showHelpCommand(op);
            return;
        }
        if(args[1].equalsIgnoreCase("setter")){
            changeSetterMode(op);
        }
        if(args[1].equalsIgnoreCase("info")){
            showWorldInfo(op);
        }
    }

    private void showHelpCommand(Player player) {
        var str = "\n" +
                "/csc world setter\n" +
                "/csc world info\n" +
                "----------";
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, str);
    }

    private void showWorldInfo(Player player) {
        var str = "\n" +
                ChaosCraft.getInstance().getConfig().getLocation("GameWorld.Size_Pos1").toString() + "\n" +
                ChaosCraft.getInstance().getConfig().getLocation("GameWorld.Size_Pos2").toString() + "\n";
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, str);
    }

    private void changeSetterMode(Player player) {
        if (checkPlayerIsSetter(player)) {
            removeSetter(player);
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.IM_NOT_SETTER);
            return;
        }
        addSetter(player);
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.IM_SETTER);
    }

    private void addSetter(Player player) {
        setters.add(player);
        tmpSaveInventory.put(player, player.getInventory().getContents());
        player.getInventory().setContents(
                emptyInventory.getContents()
        );
        player.getInventory().addItem(settingItem);
    }

    private void removeSetter(Player player){
        setters.remove(player);
        player.getInventory().setContents(tmpSaveInventory.get(player));
        tmpSaveInventory.remove(player);
    }

    public void clearSettingData(){
        setters.forEach(this::removeSetter);
        setters.clear();
        tmpSaveInventory.clear();
    }

    public void updateGameWorldPos(PlayerInteractEvent event){
        GameState cur = Game.getInstance().getGameState();
        if(!GameState.NOT_STARTED.equals(cur))
            return;
        if(!checkPlayerIsSetter(event.getPlayer()))
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
            return;
        Location location = event.getPlayer().getLocation();

        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            ConfigFileUtil.config.set("GameWorld.Size_Pos1", location);
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, event.getPlayer(), ChatColor.AQUA + " 월드 Pos1이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            ChaosCraft.getInstance().saveConfig();
            return;
        }
        ConfigFileUtil.config.set("GameWorld.Size_Pos2", event.getPlayer().getLocation());
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT ,event.getPlayer(), ChatColor.AQUA + " 월드 Pos2이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        ChaosCraft.getInstance().saveConfig();
    }
}
