package org.red;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.red.event.listener.InteractiveItemListener;
import org.red.event.listener.InteractiveTileListener;
import org.red.event.listener.area.*;
import org.red.library.CommediaDellarte;
import org.red.library.event.area.block.*;
import org.red.library.event.area.entity.*;
import org.red.library.event.area.player.*;

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
        getLogger().info("CommediaDellartePlugin enabled");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            manager = new DellarteManager();
            this.setEventListener();
            manager.entitiesDataLoad();
            CommediaDellarte.setDellarteManager(manager);
        }, 1);
    }

    @Override
    public void onDisable() {
        manager.allDataSave();
        getLogger().info("CommediaDellartePlugin disabled");
    }


    private void setEventListener() {

        new InteractiveItemListener().register();
        new InteractiveTileListener().register();




        // Register Area Block Event
        new AreaBlockEventListener<>(AreaBlockBreakEvent.class);
        new AreaBlockEventListener<>(AreaBlockBurnEvent.class);
        new AreaBlockEventListener<>(AreaBlockCanBuildEvent.class);
        new AreaBlockEventListener<>(AreaBlockCookEvent.class);
        new AreaBlockEventListener<>(AreaBlockDamageEvent.class);
        new AreaBlockEventListener<>(AreaBlockDispenseArmorEvent.class);
        new AreaBlockEventListener<>(AreaBlockDispenseEvent.class);
        new AreaBlockEventListener<>(AreaBlockDropItemEvent.class);
        new AreaBlockEventListener<>(AreaBlockExpEvent.class);
        new AreaBlockEventListener<>(AreaBlockExplodeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFadeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFertilizeEvent.class);
        new AreaBlockEventListener<>(AreaBlockFormEvent.class);
        new AreaBlockEventListener<>(AreaBlockFromToEvent.class);
        new AreaBlockEventListener<>(AreaBlockGrowEvent.class);
        new AreaBlockEventListener<>(AreaBlockIgniteEvent.class);
        new AreaBlockEventListener<>(AreaBlockMultiPlaceEvent.class);
        new AreaBlockEventListener<>(AreaBlockPhysicsEvent.class);
        new AreaBlockEventListener<>(AreaBlockPistonEvent.class);
        new AreaBlockEventListener<>(AreaBlockPistonExtendEvent.class);
        new AreaBlockEventListener<>(AreaBlockPistonRetractEvent.class);
        new AreaBlockEventListener<>(AreaBlockPlaceEvent.class);
        new AreaBlockEventListener<>(AreaBlockReceiveGameEvent.class);
        new AreaBlockEventListener<>(AreaBlockRedstoneEvent.class);
        new AreaBlockEventListener<>(AreaBlockShearEntityEvent.class);
        new AreaBlockEventListener<>(AreaBlockSpreadEvent.class);
        new AreaBlockEventListener<>(AreaCauldronLevelChangeEvent.class);
        new AreaBlockEventListener<>(AreaEntityBlockFormEvent.class);
        new AreaBlockEventListener<>(AreaFluidLevelChangeEvent.class);
        new AreaBlockEventListener<>(AreaLeavesDecayEvent.class);
        new AreaBlockEventListener<>(AreaMoistureChangeEvent.class);
        new AreaBlockEventListener<>(AreaNotePlayEvent.class);
        new AreaBlockEventListener<>(AreaSignChangeEvent.class);
        new AreaBlockEventListener<>(AreaSpongeAbsorbEvent.class);

        // Register Area Player Event
        new AreaPlayerEventListener<>(AreaAsyncPlayerChatEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerAdvancementDoneEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerAnimationEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerArmorStandManipulateEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBedEnterEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBedLeaveEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketEmptyEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketFillEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerBucketFishEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChangedMainHandEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChangedWorldEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerCommandPreprocessEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerCommandSendEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerDropItemEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerEditBookEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerEggThrowEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerExpChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerFishEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerGameModeChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerHarvestBlockEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractAtEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerInteractEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemBreakEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemConsumeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemDamageEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemHeldEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerItemMendEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerJoinEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerKickEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLevelChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLocaleChangeEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerLoginEvent.class);
        new AreaPlayerMoveEventListener();
        new AreaPlayerEventListener<>(AreaPlayerPickupArrowEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerPickupItemEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerPortalEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerQuitEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRecipeDiscoverEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRegisterChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerResourcePackStatusEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRespawnEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerRiptideEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerShearEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerStatisticIncrementEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerSwapHandItemsEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerTakeLecternBookEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerTeleportEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleFlightEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleSneakEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerToggleSprintEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerUnleashEntityEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerUnregisterChannelEvent.class);
        new AreaPlayerEventListener<>(AreaPlayerVelocityEvent.class);

        // Register Area Entity Event
        new AreaEntityEventListener<>(AreaAreaEffectCloudApplyEvent.class);
        new AreaEntityEventListener<>(AreaArrowBodyCountChangeEvent.class);
        new AreaEntityEventListener<>(AreaBatToggleSleepEvent.class);
        new AreaEntityEventListener<>(AreaCreatureSpawnEvent.class);
        new AreaEntityEventListener<>(AreaCreeperPowerEvent.class);
        new AreaEntityEventListener<>(AreaEnderDragonChangePhaseEvent.class);
        new AreaEntityEventListener<>(AreaEntityAirChangeEvent.class);
        new AreaEntityEventListener<>(AreaEntityBreakDoorEvent.class);
        new AreaEntityEventListener<>(AreaEntityBreedEvent.class);
        new AreaEntityEventListener<>(AreaEntityChangeBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustByBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustByEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityCombustEvent.class);
        new AreaEntityEventListener<>(AreaEntityCreatePortalEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageByBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageByEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityDamageEvent.class);
        new AreaEntityEventListener<>(AreaEntityDeathEvent.class);
        new AreaEntityEventListener<>(AreaEntityDropItemEvent.class);
        new AreaEntityEventListener<>(AreaEntityEnterBlockEvent.class);
        new AreaEntityEventListener<>(AreaEntityEnterLoveModeEvent.class);
        new AreaEntityEventListener<>(AreaEntityExhaustionEvent.class);
        new AreaEntityEventListener<>(AreaEntityExplodeEvent.class);
        new AreaEntityEventListener<>(AreaEntityInteractEvent.class);
        new AreaEntityEventListener<>(AreaEntityPickupItemEvent.class);
        new AreaEntityEventListener<>(AreaEntityPlaceEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalEnterEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalEvent.class);
        new AreaEntityEventListener<>(AreaEntityPortalExitEvent.class);
        new AreaEntityEventListener<>(AreaEntityPoseChangeEvent.class);
        new AreaEntityEventListener<>(AreaEntityPotionEffectEvent.class);
        new AreaEntityEventListener<>(AreaEntityRegainHealthEvent.class);
        new AreaEntityEventListener<>(AreaEntityResurrectEvent.class);
        new AreaEntityEventListener<>(AreaEntityShootBowEvent.class);
        new AreaEntityEventListener<>(AreaEntitySpawnEvent.class);
        new AreaEntityEventListener<>(AreaEntitySpellCastEvent.class);
        new AreaEntityEventListener<>(AreaEntityTameEvent.class);
        new AreaEntityEventListener<>(AreaEntityTargetEvent.class);
        new AreaEntityEventListener<>(AreaEntityTargetLivingEntityEvent.class);
        new AreaEntityEventListener<>(AreaEntityTeleportEvent.class);
        new AreaEntityEventListener<>(AreaEntityToggleGlideEvent.class);
        new AreaEntityEventListener<>(AreaEntityToggleSwimEvent.class);
        new AreaEntityEventListener<>(AreaEntityTransformEvent.class);
        new AreaEntityEventListener<>(AreaEntityUnleashEvent.class);
        new AreaEntityEventListener<>(AreaExpBottleEvent.class);
        new AreaEntityEventListener<>(AreaExplosionPrimeEvent.class);
        new AreaEntityEventListener<>(AreaFireworkExplodeEvent.class);
        new AreaEntityEventListener<>(AreaFoodLevelChangeEvent.class);
        new AreaEntityEventListener<>(AreaHorseJumpEvent.class);
        new AreaEntityEventListener<>(AreaItemDespawnEvent.class);
        new AreaEntityEventListener<>(AreaItemMergeEvent.class);
        new AreaEntityEventListener<>(AreaItemSpawnEvent.class);
        new AreaEntityEventListener<>(AreaLingeringPotionSplashEvent.class);
        new AreaEntityEventListener<>(AreaPiglinBarterEvent.class);
        new AreaEntityEventListener<>(AreaPigZapEvent.class);
        new AreaEntityEventListener<>(AreaPigZombieAngerEvent.class);
        new AreaEntityEventListener<>(AreaPlayerDeathEvent.class);
        new AreaPlayerLeashEntityEventListener();
        new AreaEntityEventListener<>(AreaPotionSplashEvent.class);
        new AreaEntityEventListener<>(AreaProjectileHitEvent.class);
        new AreaEntityEventListener<>(AreaProjectileLaunchEvent.class);
        new AreaEntityEventListener<>(AreaSheepDyeWoolEvent.class);
        new AreaEntityEventListener<>(AreaSheepRegrowWoolEvent.class);
        new AreaEntityEventListener<>(AreaSlimeSplitEvent.class);
        new AreaEntityEventListener<>(AreaSpawnerSpawnEvent.class);
        new AreaEntityEventListener<>(AreaStriderTemperatureChangeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerAcquireTradeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerCareerChangeEvent.class);
        new AreaEntityEventListener<>(AreaVillagerReplenishTradeEvent.class);
    }
}
