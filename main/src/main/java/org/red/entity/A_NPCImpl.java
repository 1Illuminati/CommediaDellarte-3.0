package org.red.entity;

import org.bukkit.entity.Player;
import org.red.library.entity.A_NPC;
import org.red.library.util.A_Data;

public class A_NPCImpl extends A_PlayerImpl implements A_NPC {
    public A_NPCImpl(Player player, A_Data data) {
        super(data, player);
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
