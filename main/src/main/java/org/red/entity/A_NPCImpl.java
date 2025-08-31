package org.red.entity;

import org.bukkit.entity.Player;
import org.red.library.entity.A_NPC;

public class A_NPCImpl extends A_PlayerImpl implements A_NPC {
    public A_NPCImpl(Player player) {
        super(player);
    }

    @Override
    public boolean isNPC() {
        return true;
    }

    @Override
    public void aDataSave() {

    }

    @Override
    public void aDataLoad() {

    }
}
