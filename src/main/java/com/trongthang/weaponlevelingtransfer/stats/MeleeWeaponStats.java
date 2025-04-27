package com.trongthang.weaponlevelingtransfer.stats;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class MeleeWeaponStats {
    public static class Stats {
        public int damage;
        public int level;
        public int levelProgress;

        public Stats(int damage, int level, int levelProgress) {
            this.damage = damage;
            this.level = level;
            this.levelProgress = levelProgress;
        }
    }

    // Extract stats from an ItemStack
    public static Stats getStats(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int damage = nbt.getInt("Damage"); // Standard durability tag
        int level = nbt.getInt("level"); // Custom level tag
        int levelProgress = nbt.getInt("levelprogress"); // Custom progress tag
        return new Stats(damage, level, levelProgress);
    }
}
