package org.sigee.chaoscraft.utils;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.sigee.chaoscraft.ChaosCraft;
import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.api.entities.enums.GameState;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : GenRequirements
 * @since : 2023/04/15
 */
public class GenRequirementsUtil {
    private static final Set<BlockVector> spawnBlockVectors = Collections.synchronizedSet(new HashSet<>());
    private static final Set<BlockVector> chestBlockVectors = Collections.synchronizedSet(new HashSet<>());
    private static final Set<BlockVector> craftingBlockVectors = Collections.synchronizedSet(new HashSet<>());
    private static final Set<BlockVector> furnaceBlockVectors = Collections.synchronizedSet(new HashSet<>());
    private static HashMap<Integer,ArrayList<AbstractMap.SimpleEntry<Material,Integer>>> changedItems = new HashMap<>();
    private static HashMap<Integer,ArrayList<Material>> changedBlocks = new HashMap<>();
    private static World world = null;

    public static void addChangedItem(Integer code, AbstractMap.SimpleEntry<Material,Integer> data){
        changedItems.computeIfAbsent(code, k -> new ArrayList<>());
        changedItems.get(code).add(data);
    }

    public static void addChangedItems(Integer code, Collection<AbstractMap.SimpleEntry<Material,Integer>> collection){
        changedItems.computeIfAbsent(code, k -> new ArrayList<>());
        changedItems.get(code).addAll(collection);
    }

    public static void clearChangedCodeItems(Integer code){
        if(changedItems.get(code) == null)
            return;
        changedItems.get(code).clear();
    }

    public static void clearChangedItems(){
        changedItems.clear();
    }

    public static void addChangedBlock(Integer code, Material material){
        changedBlocks.computeIfAbsent(code, k -> new ArrayList<>());
        changedBlocks.get(code).add(material);
    }

    public static void addChangedBlocks(Integer code, Collection<Material> collection) {
        changedBlocks.computeIfAbsent(code, k -> new ArrayList<>());
        changedBlocks.get(code).addAll(collection);
    }

    public static void clearChangedCodeBlocks(Integer code){
        if(changedBlocks.get(code) == null)
            return;
        changedBlocks.get(code).clear();
    }

    public static void clearChangedBlocks(){
        changedBlocks.clear();
    }

    public static void resetWorldBlock(){
        world = ConfigFileUtil.config.getLocation("GameWorld.Size_Pos1").getWorld();
        initSpecBlocks(spawnBlockVectors, Material.BEDROCK);
        initSpecBlocks(chestBlockVectors, Material.CHEST);
        initSpecBlocks(craftingBlockVectors, Material.CRAFTING_TABLE);
        initSpecBlocks(furnaceBlockVectors, Material.FURNACE);
        MessageUtil.printConsoleLog(ChatColor.GREEN + "월드 특정 블럭을 리셋했습니다.");
    }

    public static void resetChestBlocks(){
        world =  ConfigFileUtil.config.getLocation("GameWorld.Size_Pos1").getWorld();
        chestBlockVectors.stream()
                .map(blockVector ->(Chest) world.getBlockAt(blockVector.getBlockX(),blockVector.getBlockY(), blockVector.getBlockZ()).getState())
                .forEach(chest -> {
                    chest.getBlockInventory().setContents(Bukkit.createInventory(null, InventoryType.CHEST,"").getContents());
                });
        MessageUtil.printConsoleLog(ChatColor.GREEN + "월드 상자 내부를 초기화했습니다.");
    }

    public static void insertItemToChest(Integer code){
        if(changedItems.get(code) == null)
            return;
        Random random = new Random();
        int size = chestBlockVectors.size();
        ArrayList<Chest> chests = chestBlockVectors.stream()
                .map(blockVector -> (Chest) world.getBlockAt(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ()).getState())
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(chests);
        changedItems.get(code).forEach(pair ->{
            Material mat = pair.getKey();
            int cnt = pair.getValue();
            for(int i = cnt; i > 0; i--){
                chests.get(random.nextInt(size)).getInventory().addItem(new ItemStack(mat, 1));
            }
        });
        MessageUtil.printConsoleLog(ChatColor.GREEN + "" + code +"코드의 아이템 리스트를 창고에 배정했습니다.");
    }

    public static void makeNecessaryBlock(Integer code){
        ArrayList<BlockVector> tmpVector = new ArrayList<>(){{
            addAll(craftingBlockVectors);
            addAll(furnaceBlockVectors);
        }};
        ArrayList<BlockVector> validatedTmpVector = tmpVector.stream().filter(vector -> {
            Material mat = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).getType();
            return mat.equals(Material.CRAFTING_TABLE) || mat.equals(Material.FURNACE);
        }).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(validatedTmpVector);

        var cnt = changedBlocks.get(code).size();
        for(int i = 0; i < cnt; i++){
            validatedTmpVector.get(i).toLocation(world).getBlock().setType(changedBlocks.get(code).get(i));
        }
    }

    private static void initSpecBlocks(Set<BlockVector> set, Material mat){
        set.forEach(blockVector -> {
            setVectorBlock(blockVector, mat);
        });
    }

    private static void setVectorBlock(BlockVector vector, Material mat){
        world.getBlockAt(vector.getBlockX(),vector.getBlockY(),vector.getBlockZ()).setType(mat);
    }

    /*
    월드 스캔과 관련된 코드
     */
    private static void clearWorldBlockSets(){spawnBlockVectors.clear();chestBlockVectors.clear();craftingBlockVectors.clear();furnaceBlockVectors.clear();}
    private static void resetWorldScanData() {clearWorldBlockSets();}
    public static void scanGameWorldPos() {
        resetWorldScanData();
        MessageUtil.printMsgToPlayers(ChatMessageType.CHAT, ChatColor.AQUA + "월드 스캐닝을 시작합니다. 시간이 조금 걸립니다.");
        final Location pos1 = ConfigFileUtil.config.getLocation("GameWorld.Size_Pos1");
        final Location pos2 = ConfigFileUtil.config.getLocation("GameWorld.Size_Pos2");
        final BlockVector stV = new BlockVector(Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
        final BlockVector edV = new BlockVector(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
        MessageUtil.printConsoleLog(stV + ", " + edV);

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> chunkPoz = new ArrayList<>();
                MessageUtil.printMsgToPlayers(ChatMessageType.ACTION_BAR, "유효한 청크를 조사 중입니다.");

                for (int x = stV.getBlockX(); x <= edV.getBlockX(); x += 16)
                    for (int z = stV.getBlockZ(); z <= edV.getBlockZ(); z += 16)
                        chunkPoz.add(new AbstractMap.SimpleEntry(x >> 4, z >> 4));
                MessageUtil.printMsgToPlayers(ChatMessageType.ACTION_BAR, "청크를 조사 중입니다.");

                final AbstractMap.SimpleEntry<Integer, Integer> rRange = new AbstractMap.SimpleEntry<>(stV.getBlockY(), edV.getBlockY());
                int threadCount = Runtime.getRuntime().availableProcessors();
                Integer scanSize = chunkPoz.size();
                int scanCount = 0;
                Chunk chunk = null;
                ArrayList<ChunkSnapshot> tmpChunks = new ArrayList<>();

                for (AbstractMap.SimpleEntry<Integer, Integer> chunkPos : chunkPoz) {
                    if (!Game.getInstance().getGameState().equals(GameState.LOADING)) {
                        MessageUtil.printConsoleLog(ChatColor.RED + "맵 스캔이 종료되었습니다.");
                        break;
                    }
                    chunk = pos1.getWorld().getChunkAt(chunkPos.getKey(), chunkPos.getValue());tmpChunks.add(chunk.getChunkSnapshot());
                    if (tmpChunks.size() < threadCount)
                        continue;
                    MessageUtil.printMsgToPlayers(ChatMessageType.ACTION_BAR, ChatColor.DARK_AQUA + "Scanning: %d/%d".formatted(scanCount, scanSize));
                    scanSomeChunks(tmpChunks, rRange);tmpChunks.clear();scanCount += threadCount;}
                scanSomeChunks(tmpChunks, rRange);
                MessageUtil.printMsgToPlayers(ChatMessageType.ACTION_BAR, ScriptCode.END_WORLD_SCAN_MSG);
                MessageUtil.printConsoleLog(ChatColor.GREEN + "%s : [%d], %s : [%d], %s : [%d]".formatted("Chest", (long) chestBlockVectors.size(), Material.BEDROCK.name(), (long) spawnBlockVectors.size(), Material.FURNACE.name(), (long) furnaceBlockVectors.size()));
                this.cancel();
            }
        }.runTaskAsynchronously(ChaosCraft.getInstance());
    }
    private static void scanSomeChunks(final ArrayList<ChunkSnapshot> chunks, final AbstractMap.SimpleEntry<Integer, Integer> yRange) {chunks.parallelStream().forEach(chunk -> getBlocksInChunk(chunk, yRange));}
    private static void getBlocksInChunk(final ChunkSnapshot chunk, final AbstractMap.SimpleEntry<Integer, Integer> yRange) {Set<BlockVector> spawnBlocks = new HashSet<>();Set<BlockVector> craftingBlocks = new HashSet<>();Set<BlockVector> furnaceBlocks = new HashSet<>();Set<BlockVector> chestBlocks = new HashSet<>();final int minX = chunk.getX() << 4;final int maxX = minX | 15;final int minY = yRange.getKey();final int maxY = yRange.getValue();final int minZ = chunk.getZ() << 4;final int maxZ = minZ | 15;for (int x = minX; x <= maxX; ++x) for (int y = minY; y <= maxY; ++y) for (int z = minZ; z <= maxZ; ++z) {if (Material.BEDROCK.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) spawnBlocks.add(new BlockVector(x, y, z));if (Material.CRAFTING_TABLE.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) craftingBlocks.add(new BlockVector(x, y, z));if (Material.FURNACE.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) furnaceBlocks.add(new BlockVector(x, y, z));if (Material.CHEST.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) chestBlocks.add(new BlockVector(x, y, z));}spawnBlockVectors.addAll(spawnBlocks);craftingBlockVectors.addAll(craftingBlocks);furnaceBlockVectors.addAll(furnaceBlocks);chestBlockVectors.addAll(chestBlocks);}

}
