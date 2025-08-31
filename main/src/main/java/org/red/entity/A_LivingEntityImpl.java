package org.red.entity;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.entity.A_LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class A_LivingEntityImpl extends A_EntityImpl implements A_LivingEntity {
    private final LivingEntity entity;

    public A_LivingEntityImpl(LivingEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.entity;
    }

    @Override
    public A_LivingEntity getALivingEntity() {
        return this;
    }

    @Override
    public double getEyeHeight() {
        return entity.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean b) {
        return entity.getEyeHeight(b);
    }

    @Override
    @NotNull
    public Location getEyeLocation() {
        return entity.getEyeLocation();
    }

    @Override
    @NotNull
    public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
        return entity.getLineOfSight(set, i);
    }

    @Override
    @NotNull
    public Block getTargetBlock(@Nullable Set<Material> set, int i) {
        return entity.getTargetBlock(set, i);
    }

    @Override
    @NotNull
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
        return entity.getLastTwoTargetBlocks(set, i);
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i) {
        return entity.getTargetBlockExact(i);
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
        return entity.getTargetBlockExact(i, fluidCollisionMode);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(double v) {
        return entity.rayTraceBlocks(v);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return entity.rayTraceBlocks(v, fluidCollisionMode);
    }

    @Override
    public int getRemainingAir() {
        return entity.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int i) {
        entity.setRemainingAir(i);
    }

    @Override
    public int getMaximumAir() {
        return entity.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int i) {
        entity.setMaximumAir(i);
    }

    @Override
    public int getArrowCooldown() {
        return entity.getArrowCooldown();
    }

    @Override
    public void setArrowCooldown(int i) {
        entity.setArrowCooldown(i);
    }

    @Override
    public int getArrowsInBody() {
        return entity.getArrowsInBody();
    }

    @Override
    public void setArrowsInBody(int i) {
        entity.setArrowsInBody(i);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return entity.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int i) {
        entity.setMaximumNoDamageTicks(i);
    }

    @Override
    public double getLastDamage() {
        return entity.getLastDamage();
    }

    @Override
    public void setLastDamage(double v) {
        entity.setLastDamage(v);
    }

    @Override
    public int getNoDamageTicks() {
        return entity.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int i) {
        entity.setNoDamageTicks(i);
    }

    @Override
    @Nullable
    public Player getKiller() {
        return entity.getKiller();
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
        return entity.addPotionEffect(potionEffect);
    }

    @Override
    @Deprecated
    public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
        return entity.addPotionEffect(potionEffect, b);
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffectType var1, int duration, int amplifier) {
        return entity.addPotionEffect(new PotionEffect(var1, duration, amplifier));
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
        return entity.addPotionEffects(collection);
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return entity.hasPotionEffect(potionEffectType);
    }

    @Override
    @Nullable
    public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return entity.getPotionEffect(potionEffectType);
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
        entity.removePotionEffect(potionEffectType);
    }

    @Override
    @NotNull
    public Collection<PotionEffect> getActivePotionEffects() {
        return entity.getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity entity) {
        return this.entity.hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return entity.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean b) {
        entity.setRemoveWhenFarAway(b);
    }

    @Override
    @Nullable
    public EntityEquipment getEquipment() {
        return entity.getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean b) {
        entity.setCanPickupItems(b);
    }

    @Override
    public boolean getCanPickupItems() {
        return entity.getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return entity.isLeashed();
    }

    @Override
    @NotNull
    public Entity getLeashHolder() throws IllegalStateException {
        return entity.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(@Nullable Entity entity) {
        return this.entity.setLeashHolder(entity);
    }

    @Override
    public boolean isGliding() {
        return entity.isGliding();
    }

    @Override
    public void setGliding(boolean b) {
        entity.setGliding(b);
    }

    @Override
    public boolean isSwimming() {
        return entity.isSwimming();
    }

    @Override
    public void setSwimming(boolean b) {
        entity.setSwimming(b);
    }

    @Override
    public boolean isRiptiding() {
        return entity.isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return entity.isSleeping();
    }

    @Override
    public boolean isClimbing() {
        return entity.isClimbing();
    }

    @Override
    public void setAI(boolean b) {
        entity.setAI(b);
    }

    @Override
    public boolean hasAI() {
        return entity.hasAI();
    }

    @Override
    public void attack(@NotNull Entity entity) {
        this.entity.attack(entity);
    }

    @Override
    public void swingMainHand() {
        entity.swingMainHand();
    }

    @Override
    public void swingOffHand() {
        entity.swingOffHand();
    }

    @Override
    public void setCollidable(boolean b) {
        entity.setCollidable(b);
    }

    @Override
    public boolean isCollidable() {
        return entity.isCollidable();
    }

    @Override
    @NotNull
    public Set<UUID> getCollidableExemptions() {
        return entity.getCollidableExemptions();
    }

    @Override
    @Nullable
    public <T> T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return entity.getMemory(memoryKey);
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
        entity.setMemory(memoryKey, t);
    }

    @Override
    @NotNull
    public EntityCategory getCategory() {
        return entity.getCategory();
    }

    @Override
    public void setInvisible(boolean b) {
        entity.setInvisible(b);
    }

    @Override
    public boolean isInvisible() {
        return entity.isInvisible();
    }

    @Override
    @Nullable
    public AttributeInstance getAttribute(@NotNull Attribute attribute) {
        return entity.getAttribute(attribute);
    }

    @Override
    public void damage(double v) {
        entity.damage(v);
    }

    @Override
    public void damage(double v, @Nullable Entity entity) {
        this.entity.damage(v, entity);
    }

    @Override
    public double getHealth() {
        return entity.getHealth();
    }

    @Override
    public void setHealth(double v) {
        entity.setHealth(v);
    }

    @Override
    public double getAbsorptionAmount() {
        return entity.getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double v) {
        entity.setAbsorptionAmount(v);
    }

    @Override
    @Deprecated
    public double getMaxHealth() {
        return entity.getMaxHealth();
    }

    @Override
    @Deprecated
    public void setMaxHealth(double v) {
        entity.setMaxHealth(v);
    }

    @Override
    @Deprecated
    public void resetMaxHealth() {
        entity.resetMaxHealth();
    }

    @Override
    @NotNull
    public Location getLocation() {
        return entity.getLocation();
    }

    @Override
    @Nullable
    @Contract("null -> null; !null -> !null")
    public Location getLocation(@Nullable Location location) {
        return entity.getLocation(location);
    }

    @Override
    public void setVelocity(@NotNull Vector vector) {
        entity.setVelocity(vector);
    }

    @Override
    @NotNull
    public Vector getVelocity() {
        return entity.getVelocity();
    }

    @Override
    public double getHeight() {
        return entity.getHeight();
    }

    @Override
    public double getWidth() {
        return entity.getWidth();
    }

    @Override
    @NotNull
    public BoundingBox getBoundingBox() {
        return entity.getBoundingBox();
    }

    @Override
    public boolean isOnGround() {
        return entity.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return entity.isInWater();
    }

    @Override
    @NotNull
    public World getWorld() {
        return entity.getWorld();
    }

    @Override
    public void setRotation(float v, float v1) {
        entity.setRotation(v, v1);
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return entity.teleport(location);
    }

    @Override
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
        return entity.teleport(location, teleportCause);
    }

    @Override
    public boolean teleport(@NotNull Entity entity) {
        return this.entity.teleport(entity);
    }

    @Override
    public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
        return this.entity.teleport(entity, teleportCause);
    }

    @Override
    @NotNull
    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return entity.getNearbyEntities(v, v1, v2);
    }

    @Override
    public int getEntityId() {
        return entity.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return entity.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int i) {
        entity.setFireTicks(i);
    }

    @Override
    public void setVisualFire(boolean b) {
        entity.setVisualFire(b);
    }

    @Override
    public boolean isVisualFire() {
        return entity.isVisualFire();
    }

    @Override
    public int getFreezeTicks() {
        return entity.getFreezeTicks();
    }

    @Override
    public int getMaxFreezeTicks() {
        return entity.getMaxFreezeTicks();
    }

    @Override
    public void setFreezeTicks(int i) {
        entity.setFreezeTicks(i);
    }

    @Override
    public boolean isFrozen() {
        return entity.isFrozen();
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isDead() {
        return entity.isDead();
    }

    @Override
    public boolean isValid() {
        return entity.isValid();
    }

    @Override
    @NotNull
    public Server getServer() {
        return entity.getServer();
    }

    @Override
    public boolean isPersistent() {
        return entity.isPersistent();
    }

    @Override
    public void setPersistent(boolean b) {
        entity.setPersistent(b);
    }

    @Override
    @Nullable
    @Deprecated
    public Entity getPassenger() {
        return entity.getPassenger();
    }

    @Override
    @Deprecated
    public boolean setPassenger(@NotNull Entity entity) {
        return this.entity.setPassenger(entity);
    }

    @Override
    @NotNull
    public List<Entity> getPassengers() {
        return entity.getPassengers();
    }

    @Override
    public boolean addPassenger(@NotNull Entity entity) {
        return this.entity.addPassenger(entity);
    }

    @Override
    public boolean removePassenger(@NotNull Entity entity) {
        return this.entity.removePassenger(entity);
    }

    @Override
    public boolean isEmpty() {
        return entity.isEmpty();
    }

    @Override
    public boolean eject() {
        return entity.eject();
    }

    @Override
    public float getFallDistance() {
        return entity.getFallDistance();
    }

    @Override
    public void setFallDistance(float v) {
        entity.setFallDistance(v);
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
        entity.setLastDamageCause(entityDamageEvent);
    }

    @Override
    @Nullable
    public EntityDamageEvent getLastDamageCause() {
        return entity.getLastDamageCause();
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return entity.getTicksLived();
    }

    @Override
    public void setTicksLived(int i) {
        entity.setTicksLived(i);
    }

    @Override
    public void playEffect(@NotNull EntityEffect entityEffect) {
        entity.playEffect(entityEffect);
    }

    @Override
    @NotNull
    public EntityType getType() {
        return entity.getType();
    }

    @Override
    public boolean isInsideVehicle() {
        return entity.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return entity.leaveVehicle();
    }

    @Override
    @Nullable
    public Entity getVehicle() {
        return entity.getVehicle();
    }

    @Override
    public void setCustomNameVisible(boolean b) {
        entity.setCustomNameVisible(b);
    }

    @Override
    public boolean isCustomNameVisible() {
        return entity.isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean b) {
        entity.setGlowing(b);
    }

    @Override
    public boolean isGlowing() {
        return entity.isGlowing();
    }

    @Override
    public void setInvulnerable(boolean b) {
        entity.setInvulnerable(b);
    }

    @Override
    public boolean isInvulnerable() {
        return entity.isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return entity.isSilent();
    }

    @Override
    public void setSilent(boolean b) {
        entity.setSilent(b);
    }

    @Override
    public boolean hasGravity() {
        return entity.hasGravity();
    }

    @Override
    public void setGravity(boolean b) {
        entity.setGravity(b);
    }

    @Override
    public int getPortalCooldown() {
        return entity.getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int i) {
        entity.setPortalCooldown(i);
    }

    @Override
    @NotNull
    public Set<String> getScoreboardTags() {
        return entity.getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String s) {
        return entity.addScoreboardTag(s);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String s) {
        return entity.removeScoreboardTag(s);
    }

    @Override
    @NotNull
    public PistonMoveReaction getPistonMoveReaction() {
        return entity.getPistonMoveReaction();
    }

    @Override
    @NotNull
    public BlockFace getFacing() {
        return entity.getFacing();
    }

    @Override
    @NotNull
    public Pose getPose() {
        return entity.getPose();
    }

    @Override
    @NotNull
    public Entity.Spigot spigot() {
        return entity.spigot();
    }

    @Override
    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
        entity.setMetadata(s, metadataValue);
    }

    @Override
    @NotNull
    public List<MetadataValue> getMetadata(@NotNull String s) {
        return entity.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(@NotNull String s) {
        return entity.hasMetadata(s);
    }

    @Override
    public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
        entity.removeMetadata(s, plugin);
    }

    @Override
    public void sendMessage(@NotNull String s) {
        entity.sendMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String... strings) {
        entity.sendMessage(strings);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        entity.sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
        entity.sendMessage(uuid, strings);
    }

    @Override
    @NotNull
    public String getName() {
        return entity.getName();
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return entity.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return entity.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return entity.hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return entity.hasPermission(permission);
    }

    @Override
    @NotNull
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return entity.addAttachment(plugin, s, b);
    }

    @Override
    @NotNull
    public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return entity.addAttachment(plugin);
    }

    @Override
    @Nullable
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return entity.addAttachment(plugin, s, b, i);
    }

    @Override
    @Nullable
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return entity.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        entity.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        entity.recalculatePermissions();
    }

    @Override
    @NotNull
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return entity.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return entity.isOp();
    }

    @Override
    public void setOp(boolean b) {
        entity.setOp(b);
    }

    @Override
    @Nullable
    public String getCustomName() {
        return entity.getCustomName();
    }

    @Override
    public void setCustomName(@Nullable String s) {
        entity.setCustomName(s);
    }

    @Override
    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        return entity.getPersistentDataContainer();
    }

    @Override
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> aClass) {
        return entity.launchProjectile(aClass);
    }

    @Override
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> aClass, @Nullable Vector vector) {
        return entity.launchProjectile(aClass, vector);
    }
}
