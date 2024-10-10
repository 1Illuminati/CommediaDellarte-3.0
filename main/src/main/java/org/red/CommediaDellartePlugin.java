package org.red;

import org.bukkit.Bukkit;
import org.bukkit.block.TileState;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.red.compatibility.vault.A_Vault;
import org.red.command.A_VaultCommand;
import org.red.event.listener.InteractiveItemListener;
import org.red.event.listener.InteractiveTileListener;
import org.red.event.listener.InventoryEventListener;
import org.red.event.listener.area.*;
import org.red.interactive.InteractiveManagerImpl;
import org.red.library.CommediaDellarte;
import org.red.library.event.FirstLoadEvent;
import org.red.library.event.area.block.*;
import org.red.library.event.area.entity.*;
import org.red.library.event.area.player.*;
import org.red.library.user.Wallet;
import org.red.library.util.A_Data;
import org.red.library.util.CoolTimeMap;
import org.red.library.util.DataMap;
import org.red.library.util.NamespaceMap;

public class CommediaDellartePlugin extends JavaPlugin {
    public static CommediaDellartePlugin instance;
    public static DellarteManager manager;

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
        configurationSerializationSetting();
        getLogger().info("CommediaDellartePlugin enabled");
        Config.loadConfig(this.getConfig());
        setSoftPlugin();
        getCommand("a_economy").setExecutor(new A_VaultCommand());


        Bukkit.getScheduler().runTaskLater(this, () -> {
            manager = new DellarteManager();
            manager.entitiesDataLoad();
            manager.setInteractiveManager(ItemStack.class, new InteractiveManagerImpl<>(ItemStack.class));
            manager.setInteractiveManager(TileState.class, new InteractiveManagerImpl<>(TileState.class));
            CommediaDellarte.setDellarteManager(manager);
            this.setEventListener();

            Bukkit.getPluginManager().callEvent(new FirstLoadEvent());
        }, 1);
    }

    @Override
    public void onDisable() {
        manager.allDataSave();
        getLogger().info("CommediaDellartePlugin disabled");
    }

    private void setSoftPlugin() {
        if(softPluginCheck("Vault")) A_Vault.setEconomy();
    }

    private boolean softPluginCheck(String plName) {
        return Bukkit.getPluginManager().getPlugin(plName) != null;
    }

    private void configurationSerializationSetting() {
        ConfigurationSerialization.registerClass(DataMap.class);
        ConfigurationSerialization.registerClass(CoolTimeMap.class);
        ConfigurationSerialization.registerClass(A_Data.class);
        ConfigurationSerialization.registerClass(NamespaceMap.class);
        ConfigurationSerialization.registerClass(Wallet.class);
        sendDebugLog("Setting All ConfigurationSerialization");
    }


    private void setEventListener() {

        new InteractiveItemListener().register();
        new InteractiveTileListener().register();
        new InventoryEventListener().register();

        // Register Area Block Event
        new AreaBlockEventListener<>(AreaBlockBreakEvent.class, BlockBreakEvent.class);
        new AreaBlockEventListener<>(AreaBlockBurnEvent.class, BlockBurnEvent.class);
        new AreaBlockEventListener<>(AreaBlockCanBuildEvent.class, BlockCanBuildEvent.class);
        new AreaBlockEventListener<>(AreaBlockCookEvent.class, BlockCookEvent.class);
        new AreaBlockEventListener<>(AreaBlockDamageEvent.class, BlockDamageEvent.class);
        new AreaBlockEventListener<>(AreaBlockDispenseArmorEvent.class, BlockDispenseArmorEvent.class);
        new AreaBlockEventListener<>(AreaBlockDispenseEvent.class, BlockDispenseEvent.class);
        new AreaBlockEventListener<>(AreaBlockDropItemEvent.class, BlockDropItemEvent.class);
        new AreaBlockEventListener<>(AreaBlockExpEvent.class, BlockExpEvent.class);
        new AreaBlockEventListener<>(AreaBlockExplodeEvent.class, BlockExplodeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFadeEvent.class, BlockFadeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFertilizeEvent.class, BlockFertilizeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFormEvent.class, BlockFormEvent.class);
        new AreaBlockEventListener<>(AreaBlockFromToEvent.class, BlockFromToEvent.class);
        new AreaBlockEventListener<>(AreaBlockGrowEvent.class, BlockGrowEvent.class);
        new AreaBlockEventListener<>(AreaBlockIgniteEvent.class, BlockIgniteEvent.class);
        new AreaBlockEventListener<>(AreaBlockMultiPlaceEvent.class, BlockMultiPlaceEvent.class);
        new AreaBlockEventListener<>(AreaBlockPhysicsEvent.class, BlockPhysicsEvent.class);
        new AreaBlockEventListener<>(AreaBlockPistonExtendEvent.class, BlockPistonExtendEvent.class);
        new AreaBlockEventListener<>(AreaBlockPistonRetractEvent.class, BlockPistonRetractEvent.class);
        new AreaBlockEventListener<>(AreaBlockPlaceEvent.class, BlockPlaceEvent.class);
        new AreaBlockEventListener<>(AreaBlockReceiveGameEvent.class, BlockReceiveGameEvent.class);
        new AreaBlockEventListener<>(AreaBlockRedstoneEvent.class, BlockRedstoneEvent.class);
        new AreaBlockEventListener<>(AreaBlockShearEntityEvent.class, BlockShearEntityEvent.class);
        new AreaBlockEventListener<>(AreaBlockSpreadEvent.class, BlockSpreadEvent.class);
        new AreaBlockEventListener<>(AreaCauldronLevelChangeEvent.class, CauldronLevelChangeEvent.class);
        new AreaBlockEventListener<>(AreaEntityBlockFormEvent.class, EntityBlockFormEvent.class);
        new AreaBlockEventListener<>(AreaFluidLevelChangeEvent.class, FluidLevelChangeEvent.class);
        new AreaBlockEventListener<>(AreaLeavesDecayEvent.class, LeavesDecayEvent.class);
        new AreaBlockEventListener<>(AreaMoistureChangeEvent.class, MoistureChangeEvent.class);
        new AreaBlockEventListener<>(AreaNotePlayEvent.class, NotePlayEvent.class);
        new AreaBlockEventListener<>(AreaSignChangeEvent.class, SignChangeEvent.class);
        new AreaBlockEventListener<>(AreaSpongeAbsorbEvent.class, SpongeAbsorbEvent.class);


        // Register Area Player Event
        new AreaPlayerEventListener<>(AreaAsyncPlayerChatEvent.class, AsyncPlayerChatEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerAdvancementDoneEvent.class, PlayerAdvancementDoneEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerAnimationEvent.class, PlayerAnimationEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerArmorStandManipulateEvent.class, PlayerArmorStandManipulateEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBedEnterEvent.class, PlayerBedEnterEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBedLeaveEvent.class, PlayerBedLeaveEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketEmptyEvent.class, PlayerBucketEmptyEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketEntityEvent.class, PlayerBucketEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketFillEvent.class, PlayerBucketFillEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketFishEvent.class, PlayerBucketFishEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChangedMainHandEvent.class, PlayerChangedMainHandEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChangedWorldEvent.class, PlayerChangedWorldEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChannelEvent.class, PlayerChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerCommandPreprocessEvent.class, PlayerCommandPreprocessEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerCommandSendEvent.class, PlayerCommandSendEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerDropItemEvent.class, PlayerDropItemEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerEditBookEvent.class, PlayerEditBookEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerEggThrowEvent.class, PlayerEggThrowEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerExpChangeEvent.class, PlayerExpChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerFishEvent.class, PlayerFishEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerGameModeChangeEvent.class, PlayerGameModeChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerHarvestBlockEvent.class, PlayerHarvestBlockEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractAtEntityEvent.class, PlayerInteractAtEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractEntityEvent.class, PlayerInteractEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractEvent.class, PlayerInteractEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemBreakEvent.class, PlayerItemBreakEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemConsumeEvent.class, PlayerItemConsumeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemDamageEvent.class, PlayerItemDamageEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemHeldEvent.class, PlayerItemHeldEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemMendEvent.class, PlayerItemMendEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerJoinEvent.class, PlayerJoinEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerKickEvent.class, PlayerKickEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLevelChangeEvent.class, PlayerLevelChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLocaleChangeEvent.class, PlayerLocaleChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLoginEvent.class, PlayerLoginEvent.class);
        new AreaPlayerMoveEventListener();
        new AreaPlayerEventListener<>(AreaPlayerPickupArrowEvent.class, PlayerPickupArrowEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerPickupItemEvent.class, PlayerPickupItemEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerPortalEvent.class, PlayerPortalEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerQuitEvent.class, PlayerQuitEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRecipeDiscoverEvent.class, PlayerRecipeDiscoverEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRegisterChannelEvent.class, PlayerRegisterChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerResourcePackStatusEvent.class, PlayerResourcePackStatusEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRespawnEvent.class, PlayerRespawnEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRiptideEvent.class, PlayerRiptideEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerShearEntityEvent.class, PlayerShearEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerStatisticIncrementEvent.class, PlayerStatisticIncrementEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerSwapHandItemsEvent.class, PlayerSwapHandItemsEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerTakeLecternBookEvent.class, PlayerTakeLecternBookEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerTeleportEvent.class, PlayerTeleportEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleFlightEvent.class, PlayerToggleFlightEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleSneakEvent.class, PlayerToggleSneakEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleSprintEvent.class, PlayerToggleSprintEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerUnregisterChannelEvent.class, PlayerUnregisterChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerVelocityEvent.class, PlayerVelocityEvent.class);


        // Register Area Entity Event
        new AreaEntityEventListener<>(AreaAreaEffectCloudApplyEvent.class, AreaEffectCloudApplyEvent.class);
        new AreaEntityEventListener<>(AreaArrowBodyCountChangeEvent.class, ArrowBodyCountChangeEvent.class);
        new AreaEntityEventListener<>(AreaBatToggleSleepEvent.class, BatToggleSleepEvent.class);
        new AreaEntityEventListener<>(AreaCreatureSpawnEvent.class, CreatureSpawnEvent.class);
        new AreaEntityEventListener<>(AreaCreeperPowerEvent.class, CreeperPowerEvent.class);
        new AreaEntityEventListener<>(AreaEnderDragonChangePhaseEvent.class, EnderDragonChangePhaseEvent.class);
        new AreaEntityEventListener<>(AreaEntityAirChangeEvent.class, EntityAirChangeEvent.class);
        new AreaEntityEventListener<>(AreaEntityBreakDoorEvent.class, EntityBreakDoorEvent.class);
        new AreaEntityEventListener<>(AreaEntityBreedEvent.class, EntityBreedEvent.class);
        new AreaEntityEventListener<>(AreaEntityChangeBlockEvent.class, EntityChangeBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustByBlockEvent.class, EntityCombustByBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustByEntityEvent.class, EntityCombustByEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustEvent.class, EntityCombustEvent.class);
        new AreaEntityEventListener<>(AreaEntityCreatePortalEvent.class, EntityCreatePortalEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageByBlockEvent.class, EntityDamageByBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageByEntityEvent.class, EntityDamageByEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageEvent.class, EntityDamageEvent.class);
        new AreaEntityEventListener<>(AreaEntityDeathEvent.class, EntityDeathEvent.class);
        new AreaEntityEventListener<>(AreaEntityDropItemEvent.class, EntityDropItemEvent.class);
        new AreaEntityEventListener<>(AreaEntityEnterBlockEvent.class, EntityEnterBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityEnterLoveModeEvent.class, EntityEnterLoveModeEvent.class);
        new AreaEntityEventListener<>(AreaEntityExhaustionEvent.class, EntityExhaustionEvent.class);
        new AreaEntityEventListener<>(AreaEntityExplodeEvent.class, EntityExplodeEvent.class);
        new AreaEntityEventListener<>(AreaEntityInteractEvent.class, EntityInteractEvent.class);
        new AreaEntityEventListener<>(AreaEntityPickupItemEvent.class, EntityPickupItemEvent.class);
        new AreaEntityEventListener<>(AreaEntityPlaceEvent.class, EntityPlaceEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalEnterEvent.class, EntityPortalEnterEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalEvent.class, EntityPortalEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalExitEvent.class, EntityPortalExitEvent.class);
        new AreaEntityEventListener<>(AreaEntityPoseChangeEvent.class, EntityPoseChangeEvent.class);
        new AreaEntityEventListener<>(AreaEntityPotionEffectEvent.class, EntityPotionEffectEvent.class);
        new AreaEntityEventListener<>(AreaEntityRegainHealthEvent.class, EntityRegainHealthEvent.class);
        new AreaEntityEventListener<>(AreaEntityResurrectEvent.class, EntityResurrectEvent.class);
        new AreaEntityEventListener<>(AreaEntityShootBowEvent.class, EntityShootBowEvent.class);
        new AreaEntityEventListener<>(AreaEntitySpawnEvent.class, EntitySpawnEvent.class);
        new AreaEntityEventListener<>(AreaEntitySpellCastEvent.class, EntitySpellCastEvent.class);
        new AreaEntityEventListener<>(AreaEntityTameEvent.class, EntityTameEvent.class);
        new AreaEntityEventListener<>(AreaEntityTargetEvent.class, EntityTargetEvent.class);
        new AreaEntityEventListener<>(AreaEntityTargetLivingEntityEvent.class, EntityTargetLivingEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityTeleportEvent.class, EntityTeleportEvent.class);
        new AreaEntityEventListener<>(AreaEntityToggleGlideEvent.class, EntityToggleGlideEvent.class);
        new AreaEntityEventListener<>(AreaEntityToggleSwimEvent.class, EntityToggleSwimEvent.class);
        new AreaEntityEventListener<>(AreaEntityTransformEvent.class, EntityTransformEvent.class);
        new AreaEntityEventListener<>(AreaEntityUnleashEvent.class, EntityUnleashEvent.class);
        new AreaEntityEventListener<>(AreaExpBottleEvent.class, ExpBottleEvent.class);
        new AreaEntityEventListener<>(AreaExplosionPrimeEvent.class, ExplosionPrimeEvent.class);
        new AreaEntityEventListener<>(AreaFireworkExplodeEvent.class, FireworkExplodeEvent.class);
        new AreaEntityEventListener<>(AreaFoodLevelChangeEvent.class, FoodLevelChangeEvent.class);
        new AreaEntityEventListener<>(AreaHorseJumpEvent.class, HorseJumpEvent.class);
        new AreaEntityEventListener<>(AreaItemDespawnEvent.class, ItemDespawnEvent.class);
        new AreaEntityEventListener<>(AreaItemMergeEvent.class, ItemMergeEvent.class);
        new AreaEntityEventListener<>(AreaItemSpawnEvent.class, ItemSpawnEvent.class);
        new AreaEntityEventListener<>(AreaLingeringPotionSplashEvent.class, LingeringPotionSplashEvent.class);
        new AreaEntityEventListener<>(AreaPiglinBarterEvent.class, PiglinBarterEvent.class);
        new AreaEntityEventListener<>(AreaPigZapEvent.class, PigZapEvent.class);
        new AreaEntityEventListener<>(AreaPigZombieAngerEvent.class, PigZombieAngerEvent.class);
        new AreaEntityEventListener<>(AreaPlayerDeathEvent.class, PlayerDeathEvent.class);
        new AreaPlayerLeashEntityEventListener();
        new AreaEntityEventListener<>(AreaPlayerUnleashEntityEvent.class, PlayerUnleashEntityEvent.class);
        new AreaEntityEventListener<>(AreaPotionSplashEvent.class, PotionSplashEvent.class);
        new AreaEntityEventListener<>(AreaProjectileHitEvent.class, ProjectileHitEvent.class);
        new AreaEntityEventListener<>(AreaProjectileLaunchEvent.class, ProjectileLaunchEvent.class);
        new AreaEntityEventListener<>(AreaSheepDyeWoolEvent.class, SheepDyeWoolEvent.class);
        new AreaEntityEventListener<>(AreaSheepRegrowWoolEvent.class, SheepRegrowWoolEvent.class);
        new AreaEntityEventListener<>(AreaSlimeSplitEvent.class, SlimeSplitEvent.class);
        new AreaEntityEventListener<>(AreaSpawnerSpawnEvent.class, SpawnerSpawnEvent.class);
        new AreaEntityEventListener<>(AreaStriderTemperatureChangeEvent.class, StriderTemperatureChangeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerAcquireTradeEvent.class, VillagerAcquireTradeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerCareerChangeEvent.class, VillagerCareerChangeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerReplenishTradeEvent.class, VillagerReplenishTradeEvent.class);

    }
}
