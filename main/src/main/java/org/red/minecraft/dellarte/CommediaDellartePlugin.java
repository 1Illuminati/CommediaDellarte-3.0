package org.red.minecraft.dellarte;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.red.minecraft.dellarte.event.listener.InteractiveItemListener;
import org.red.minecraft.dellarte.event.listener.InteractiveTileListener;
import org.red.minecraft.dellarte.event.listener.InventoryEventListener;
import org.red.minecraft.dellarte.event.listener.PlayerChatListener;
import org.red.minecraft.dellarte.event.listener.area.*;
import org.red.minecraft.dellarte.interactive.*;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.serialize.BoundingBoxSerialize;
import org.red.minecraft.dellarte.library.data.serialize.ItemStackSerialize;
import org.red.minecraft.dellarte.library.data.serialize.LocationSerialize;
import org.red.minecraft.dellarte.library.data.serialize.VectorSerialize;
import org.red.minecraft.dellarte.library.event.FirstLoadEvent;
import org.red.minecraft.dellarte.library.event.area.block.*;
import org.red.minecraft.dellarte.library.event.area.entity.*;
import org.red.minecraft.dellarte.library.event.area.player.*;

public class CommediaDellartePlugin extends JavaPlugin {
    public static CommediaDellartePlugin instance;
    public static DellarteManager manager;
    public static FileConfiguration config;

    public static void sendLog(Object message) {
        Bukkit.getLogger().info("[ CommediaDellarte ]: " + message.toString());
    }

    public static void sendDebugLog(Object message) {
        Bukkit.getLogger().info("[ CommediaDellarte Debug ]: " + message.toString());
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getLogger().info("CommediaDellartePlugin enabled");
        config = this.getConfig();
        Config.loadConfig(config);

        manager = new DellarteManager();
        manager.setInteractiveManager(ItemStack.class, new InteractiveManagerImpl<>(ItemStack.class));
        manager.setInteractiveManager(TileState.class, new InteractiveManagerImpl<>(TileState.class));
        CommediaDellarte.setDellarteManager(manager);
        this.setEventListener();

        manager.registerSerializableClass(ItemStack.class, new ItemStackSerialize());
        manager.registerSerializableClass(Location.class, new LocationSerialize());
        manager.registerSerializableClass(BoundingBox.class, new BoundingBoxSerialize());
        manager.registerSerializableClass(Vector.class, new VectorSerialize());

        Bukkit.getScheduler().runTaskLater(this, () -> {
            CommediaDellartePlugin.manager.createDataStorages();
            CommediaDellartePlugin.manager.allDataLoad();
            Bukkit.getPluginManager().callEvent(new FirstLoadEvent());
        }, 1);
    }

    @Override
    public void onDisable() {
        manager.allDataSave();
        getLogger().info("CommediaDellartePlugin disabled");
    }

    private boolean softPluginCheck(String plName) {
        return Bukkit.getPluginManager().getPlugin(plName) != null;
    }

    private void setEventListener() {

        new InteractiveItemListener().register();
        new InteractiveTileListener().register();
        new InventoryEventListener().register();
        new PlayerChatListener().register();

        // Register Area Block Event
        new AllBlockEventListener<>(AreaBlockBreakEvent.class, BlockBreakEvent.class);
        new AllBlockEventListener<>(AreaBlockBurnEvent.class, BlockBurnEvent.class);
        new AllBlockEventListener<>(AreaBlockCanBuildEvent.class, BlockCanBuildEvent.class);
        new AllBlockEventListener<>(AreaBlockCookEvent.class, BlockCookEvent.class);
        new AllBlockEventListener<>(AreaBlockDamageEvent.class, BlockDamageEvent.class);
        new AllBlockEventListener<>(AreaBlockDispenseArmorEvent.class, BlockDispenseArmorEvent.class);
        new AllBlockEventListener<>(AreaBlockDispenseEvent.class, BlockDispenseEvent.class);
        new AllBlockEventListener<>(AreaBlockDropItemEvent.class, BlockDropItemEvent.class);
        new AllBlockEventListener<>(AreaBlockExpEvent.class, BlockExpEvent.class);
        new AllBlockEventListener<>(AreaBlockExplodeEvent.class, BlockExplodeEvent.class);
        new AllBlockEventListener<>(AreaBlockFadeEvent.class, BlockFadeEvent.class);
        new AllBlockEventListener<>(AreaBlockFertilizeEvent.class, BlockFertilizeEvent.class);
        new AllBlockEventListener<>(AreaBlockFormEvent.class, BlockFormEvent.class);
        new AllBlockEventListener<>(AreaBlockFromToEvent.class, BlockFromToEvent.class);
        new AllBlockEventListener<>(AreaBlockGrowEvent.class, BlockGrowEvent.class);
        new AllBlockEventListener<>(AreaBlockIgniteEvent.class, BlockIgniteEvent.class);
        new AllBlockEventListener<>(AreaBlockMultiPlaceEvent.class, BlockMultiPlaceEvent.class);
        new AllBlockEventListener<>(AreaBlockPhysicsEvent.class, BlockPhysicsEvent.class);
        new AllBlockEventListener<>(AreaBlockPistonExtendEvent.class, BlockPistonExtendEvent.class);
        new AllBlockEventListener<>(AreaBlockPistonRetractEvent.class, BlockPistonRetractEvent.class);
        new AllBlockEventListener<>(AreaBlockPlaceEvent.class, BlockPlaceEvent.class);
        new AllBlockEventListener<>(AreaBlockReceiveGameEvent.class, BlockReceiveGameEvent.class);
        new AllBlockEventListener<>(AreaBlockRedstoneEvent.class, BlockRedstoneEvent.class);
        new AllBlockEventListener<>(AreaBlockShearEntityEvent.class, BlockShearEntityEvent.class);
        new AllBlockEventListener<>(AreaBlockSpreadEvent.class, BlockSpreadEvent.class);
        new AllBlockEventListener<>(AreaCauldronLevelChangeEvent.class, CauldronLevelChangeEvent.class);
        new AllBlockEventListener<>(AreaEntityBlockFormEvent.class, EntityBlockFormEvent.class);
        new AllBlockEventListener<>(AreaFluidLevelChangeEvent.class, FluidLevelChangeEvent.class);
        new AllBlockEventListener<>(AreaLeavesDecayEvent.class, LeavesDecayEvent.class);
        new AllBlockEventListener<>(AreaMoistureChangeEvent.class, MoistureChangeEvent.class);
        new AllBlockEventListener<>(AreaNotePlayEvent.class, NotePlayEvent.class);
        new AllBlockEventListener<>(AreaSignChangeEvent.class, SignChangeEvent.class);
        new AllBlockEventListener<>(AreaSpongeAbsorbEvent.class, SpongeAbsorbEvent.class);


        // Register Area Player Event
        new AllPlayerEventListener<>(AreaAsyncPlayerChatEvent.class, AsyncPlayerChatEvent.class);
        new AllPlayerEventListener<>(AreaPlayerAdvancementDoneEvent.class, PlayerAdvancementDoneEvent.class);
        new AllPlayerEventListener<>(AreaPlayerAnimationEvent.class, PlayerAnimationEvent.class);
        new AllPlayerEventListener<>(AreaPlayerArmorStandManipulateEvent.class, PlayerArmorStandManipulateEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBedEnterEvent.class, PlayerBedEnterEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBedLeaveEvent.class, PlayerBedLeaveEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBucketEmptyEvent.class, PlayerBucketEmptyEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBucketEntityEvent.class, PlayerBucketEntityEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBucketFillEvent.class, PlayerBucketFillEvent.class);
        new AllPlayerEventListener<>(AreaPlayerBucketFishEvent.class, PlayerBucketFishEvent.class);
        new AllPlayerEventListener<>(AreaPlayerChangedMainHandEvent.class, PlayerChangedMainHandEvent.class);
        new AllPlayerEventListener<>(AreaPlayerChangedWorldEvent.class, PlayerChangedWorldEvent.class);
        new AllPlayerEventListener<>(AreaPlayerChannelEvent.class, PlayerChannelEvent.class);
        new AllPlayerEventListener<>(AreaPlayerCommandPreprocessEvent.class, PlayerCommandPreprocessEvent.class);
        new AllPlayerEventListener<>(AreaPlayerCommandSendEvent.class, PlayerCommandSendEvent.class);
        new AllPlayerEventListener<>(AreaPlayerDropItemEvent.class, PlayerDropItemEvent.class);
        new AllPlayerEventListener<>(AreaPlayerEditBookEvent.class, PlayerEditBookEvent.class);
        new AllPlayerEventListener<>(AreaPlayerEggThrowEvent.class, PlayerEggThrowEvent.class);
        new AllPlayerEventListener<>(AreaPlayerExpChangeEvent.class, PlayerExpChangeEvent.class);
        new AllPlayerEventListener<>(AreaPlayerFishEvent.class, PlayerFishEvent.class);
        new AllPlayerEventListener<>(AreaPlayerGameModeChangeEvent.class, PlayerGameModeChangeEvent.class);
        new AllPlayerEventListener<>(AreaPlayerHarvestBlockEvent.class, PlayerHarvestBlockEvent.class);
        new AllPlayerEventListener<>(AreaPlayerInteractAtEntityEvent.class, PlayerInteractAtEntityEvent.class);
        new AllPlayerEventListener<>(AreaPlayerInteractEntityEvent.class, PlayerInteractEntityEvent.class);
        new AllPlayerEventListener<>(AreaPlayerInteractEvent.class, PlayerInteractEvent.class);
        new AllPlayerEventListener<>(AreaPlayerItemBreakEvent.class, PlayerItemBreakEvent.class);
        new AllPlayerEventListener<>(AreaPlayerItemConsumeEvent.class, PlayerItemConsumeEvent.class);
        new AllPlayerEventListener<>(AreaPlayerItemDamageEvent.class, PlayerItemDamageEvent.class);
        new AllPlayerEventListener<>(AreaPlayerItemHeldEvent.class, PlayerItemHeldEvent.class);
        new AllPlayerEventListener<>(AreaPlayerItemMendEvent.class, PlayerItemMendEvent.class);
        new AllPlayerEventListener<>(AreaPlayerJoinEvent.class, PlayerJoinEvent.class);
        new AllPlayerEventListener<>(AreaPlayerKickEvent.class, PlayerKickEvent.class);
        new AllPlayerEventListener<>(AreaPlayerLevelChangeEvent.class, PlayerLevelChangeEvent.class);
        new AllPlayerEventListener<>(AreaPlayerLocaleChangeEvent.class, PlayerLocaleChangeEvent.class);
        new AllPlayerEventListener<>(AreaPlayerLoginEvent.class, PlayerLoginEvent.class);
        new PlayerMoveEventListener();
        new AllPlayerEventListener<>(AreaPlayerPickupArrowEvent.class, PlayerPickupArrowEvent.class);
        new AllPlayerEventListener<>(AreaPlayerPickupItemEvent.class, PlayerPickupItemEvent.class);
        new AllPlayerEventListener<>(AreaPlayerPortalEvent.class, PlayerPortalEvent.class);
        new AllPlayerEventListener<>(AreaPlayerQuitEvent.class, PlayerQuitEvent.class);
        new AllPlayerEventListener<>(AreaPlayerRecipeDiscoverEvent.class, PlayerRecipeDiscoverEvent.class);
        new AllPlayerEventListener<>(AreaPlayerRegisterChannelEvent.class, PlayerRegisterChannelEvent.class);
        new AllPlayerEventListener<>(AreaPlayerResourcePackStatusEvent.class, PlayerResourcePackStatusEvent.class);
        new AllPlayerEventListener<>(AreaPlayerRespawnEvent.class, PlayerRespawnEvent.class);
        new AllPlayerEventListener<>(AreaPlayerRiptideEvent.class, PlayerRiptideEvent.class);
        new AllPlayerEventListener<>(AreaPlayerShearEntityEvent.class, PlayerShearEntityEvent.class);
        new AllPlayerEventListener<>(AreaPlayerStatisticIncrementEvent.class, PlayerStatisticIncrementEvent.class);
        new AllPlayerEventListener<>(AreaPlayerSwapHandItemsEvent.class, PlayerSwapHandItemsEvent.class);
        new AllPlayerEventListener<>(AreaPlayerTakeLecternBookEvent.class, PlayerTakeLecternBookEvent.class);
        new AllPlayerEventListener<>(AreaPlayerTeleportEvent.class, PlayerTeleportEvent.class);
        new AllPlayerEventListener<>(AreaPlayerToggleFlightEvent.class, PlayerToggleFlightEvent.class);
        new AllPlayerEventListener<>(AreaPlayerToggleSneakEvent.class, PlayerToggleSneakEvent.class);
        new AllPlayerEventListener<>(AreaPlayerToggleSprintEvent.class, PlayerToggleSprintEvent.class);
        new AllPlayerEventListener<>(AreaPlayerUnregisterChannelEvent.class, PlayerUnregisterChannelEvent.class);
        new AllPlayerEventListener<>(AreaPlayerVelocityEvent.class, PlayerVelocityEvent.class);


        // Register Area Entity Event
        new AllEntityEventListener<>(AreaAreaEffectCloudApplyEvent.class, AreaEffectCloudApplyEvent.class);
        new AllEntityEventListener<>(AreaArrowBodyCountChangeEvent.class, ArrowBodyCountChangeEvent.class);
        new AllEntityEventListener<>(AreaBatToggleSleepEvent.class, BatToggleSleepEvent.class);
        new AllEntityEventListener<>(AreaCreatureSpawnEvent.class, CreatureSpawnEvent.class);
        new AllEntityEventListener<>(AreaCreeperPowerEvent.class, CreeperPowerEvent.class);
        new AllEntityEventListener<>(AreaEnderDragonChangePhaseEvent.class, EnderDragonChangePhaseEvent.class);
        new AllEntityEventListener<>(AreaEntityAirChangeEvent.class, EntityAirChangeEvent.class);
        new AllEntityEventListener<>(AreaEntityBreakDoorEvent.class, EntityBreakDoorEvent.class);
        new AllEntityEventListener<>(AreaEntityBreedEvent.class, EntityBreedEvent.class);
        new AllEntityEventListener<>(AreaEntityChangeBlockEvent.class, EntityChangeBlockEvent.class);
        new AllEntityEventListener<>(AreaEntityCombustByBlockEvent.class, EntityCombustByBlockEvent.class);
        new AllEntityEventListener<>(AreaEntityCombustByEntityEvent.class, EntityCombustByEntityEvent.class);
        new AllEntityEventListener<>(AreaEntityCombustEvent.class, EntityCombustEvent.class);
        new AllEntityEventListener<>(AreaEntityCreatePortalEvent.class, EntityCreatePortalEvent.class);
        new AllEntityEventListener<>(AreaEntityDamageByBlockEvent.class, EntityDamageByBlockEvent.class);
        new AllEntityEventListener<>(AreaEntityDamageByEntityEvent.class, EntityDamageByEntityEvent.class);
        new AllEntityEventListener<>(AreaEntityDamageEvent.class, EntityDamageEvent.class);
        new AllEntityEventListener<>(AreaEntityDeathEvent.class, EntityDeathEvent.class);
        new AllEntityEventListener<>(AreaEntityDropItemEvent.class, EntityDropItemEvent.class);
        new AllEntityEventListener<>(AreaEntityEnterBlockEvent.class, EntityEnterBlockEvent.class);
        new AllEntityEventListener<>(AreaEntityEnterLoveModeEvent.class, EntityEnterLoveModeEvent.class);
        new AllEntityEventListener<>(AreaEntityExhaustionEvent.class, EntityExhaustionEvent.class);
        new AllEntityEventListener<>(AreaEntityExplodeEvent.class, EntityExplodeEvent.class);
        new AllEntityEventListener<>(AreaEntityInteractEvent.class, EntityInteractEvent.class);
        new AllEntityEventListener<>(AreaEntityPickupItemEvent.class, EntityPickupItemEvent.class);
        new AllEntityEventListener<>(AreaEntityPlaceEvent.class, EntityPlaceEvent.class);
        new AllEntityEventListener<>(AreaEntityPortalEnterEvent.class, EntityPortalEnterEvent.class);
        new AllEntityEventListener<>(AreaEntityPortalEvent.class, EntityPortalEvent.class);
        new AllEntityEventListener<>(AreaEntityPortalExitEvent.class, EntityPortalExitEvent.class);
        new AllEntityEventListener<>(AreaEntityPoseChangeEvent.class, EntityPoseChangeEvent.class);
        new AllEntityEventListener<>(AreaEntityPotionEffectEvent.class, EntityPotionEffectEvent.class);
        new AllEntityEventListener<>(AreaEntityRegainHealthEvent.class, EntityRegainHealthEvent.class);
        new AllEntityEventListener<>(AreaEntityResurrectEvent.class, EntityResurrectEvent.class);
        new AllEntityEventListener<>(AreaEntityShootBowEvent.class, EntityShootBowEvent.class);
        new AllEntityEventListener<>(AreaEntitySpawnEvent.class, EntitySpawnEvent.class);
        new AllEntityEventListener<>(AreaEntitySpellCastEvent.class, EntitySpellCastEvent.class);
        new AllEntityEventListener<>(AreaEntityTameEvent.class, EntityTameEvent.class);
        new AllEntityEventListener<>(AreaEntityTargetEvent.class, EntityTargetEvent.class);
        new AllEntityEventListener<>(AreaEntityTargetLivingEntityEvent.class, EntityTargetLivingEntityEvent.class);
        new AllEntityEventListener<>(AreaEntityTeleportEvent.class, EntityTeleportEvent.class);
        new AllEntityEventListener<>(AreaEntityToggleGlideEvent.class, EntityToggleGlideEvent.class);
        new AllEntityEventListener<>(AreaEntityToggleSwimEvent.class, EntityToggleSwimEvent.class);
        new AllEntityEventListener<>(AreaEntityTransformEvent.class, EntityTransformEvent.class);
        new AllEntityEventListener<>(AreaEntityUnleashEvent.class, EntityUnleashEvent.class);
        new AllEntityEventListener<>(AreaExpBottleEvent.class, ExpBottleEvent.class);
        new AllEntityEventListener<>(AreaExplosionPrimeEvent.class, ExplosionPrimeEvent.class);
        new AllEntityEventListener<>(AreaFireworkExplodeEvent.class, FireworkExplodeEvent.class);
        new AllEntityEventListener<>(AreaFoodLevelChangeEvent.class, FoodLevelChangeEvent.class);
        new AllEntityEventListener<>(AreaHorseJumpEvent.class, HorseJumpEvent.class);
        new AllEntityEventListener<>(AreaItemDespawnEvent.class, ItemDespawnEvent.class);
        new AllEntityEventListener<>(AreaItemMergeEvent.class, ItemMergeEvent.class);
        new AllEntityEventListener<>(AreaItemSpawnEvent.class, ItemSpawnEvent.class);
        new AllEntityEventListener<>(AreaLingeringPotionSplashEvent.class, LingeringPotionSplashEvent.class);
        new AllEntityEventListener<>(AreaPiglinBarterEvent.class, PiglinBarterEvent.class);
        new AllEntityEventListener<>(AreaPigZapEvent.class, PigZapEvent.class);
        new AllEntityEventListener<>(AreaPigZombieAngerEvent.class, PigZombieAngerEvent.class);
        new AllEntityEventListener<>(AreaPlayerDeathEvent.class, PlayerDeathEvent.class);
        new PlayerLeashEntityEventListener();
        new AllEntityEventListener<>(AreaPlayerUnleashEntityEvent.class, PlayerUnleashEntityEvent.class);
        new AllEntityEventListener<>(AreaPotionSplashEvent.class, PotionSplashEvent.class);
        new AllEntityEventListener<>(AreaProjectileHitEvent.class, ProjectileHitEvent.class);
        new AllEntityEventListener<>(AreaProjectileLaunchEvent.class, ProjectileLaunchEvent.class);
        new AllEntityEventListener<>(AreaSheepDyeWoolEvent.class, SheepDyeWoolEvent.class);
        new AllEntityEventListener<>(AreaSheepRegrowWoolEvent.class, SheepRegrowWoolEvent.class);
        new AllEntityEventListener<>(AreaSlimeSplitEvent.class, SlimeSplitEvent.class);
        new AllEntityEventListener<>(AreaSpawnerSpawnEvent.class, SpawnerSpawnEvent.class);
        new AllEntityEventListener<>(AreaStriderTemperatureChangeEvent.class, StriderTemperatureChangeEvent.class);
        new AllEntityEventListener<>(AreaVillagerAcquireTradeEvent.class, VillagerAcquireTradeEvent.class);
        new AllEntityEventListener<>(AreaVillagerCareerChangeEvent.class, VillagerCareerChangeEvent.class);
        new AllEntityEventListener<>(AreaVillagerReplenishTradeEvent.class, VillagerReplenishTradeEvent.class);

    }
}
