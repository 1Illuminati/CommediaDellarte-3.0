package org.red.library.skill;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.red.library.entity.A_Player;
import org.red.library.interactive.InteractiveAct;
import org.red.library.interactive.InteractiveObj;

public abstract class Skill implements InteractiveObj<A_Player> {
    @Override
    public void setInteractiveInObj(A_Player player) {
        player.getDataMap().getDataMap("skills").put(getKey().toString(), "Skill");
    }

    @Override
    public boolean isHasInteractive(A_Player player) {
        return player.getDataMap().getDataMap("skills").containsKey(getKey().toString());
    }

    @Override
    public void removeInteractive(A_Player player) {
        player.getDataMap().getDataMap("skills").remove(getKey().toString());
    }

    @SkillAct.ActAnnotation(event = PlayerInteractEvent.class)
    public static class LEFT_CLICK implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerInteractEvent.class)
    public static class RIGHT_CLICK implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerInteractEvent.class)
    public static class SHIFT_LEFT_CLICK implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerInteractEvent.class)
    public static class SHIFT_RIGHT_CLICK implements SkillAct {}

    @SkillAct.ActAnnotation(event = BlockBreakEvent.class)
    public static class BREAK implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerFishEvent.class)
    public static class FISH implements SkillAct {}

    @SkillAct.ActAnnotation(event = EntityDamageByEntityEvent.class)
    public static class HIT implements SkillAct {}

    @SkillAct.ActAnnotation(event = EntityDamageByEntityEvent.class)
    public static class DAMAGED implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerDropItemEvent.class)
    public static class DROP implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerSwapHandItemsEvent.class)
    public static class SWAP_HAND implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerDropItemEvent.class)
    public static class SHIFT_DROP implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerSwapHandItemsEvent.class)
    public static class SHIFT_SWAP_HAND implements SkillAct {}

    @SkillAct.ActAnnotation(event = PlayerDeathEvent.class)
    public static class DEATH implements SkillAct {}

    interface SkillAct extends InteractiveAct<A_Player> {

    }
}
