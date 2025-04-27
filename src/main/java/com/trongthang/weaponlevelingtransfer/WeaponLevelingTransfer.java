package com.trongthang.weaponlevelingtransfer;

import com.trongthang.weaponlevelingtransfer.stats.MeleeWeaponStats;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponLevelingTransfer implements ModInitializer {
    public static final String MOD_ID = "weaponlevelingtransfer";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigLoader.loadConfig();
    }
}