package com.trongthang.weaponlevelingtransfer;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static KeyBinding LOG_ITEM_KEY;

    public static void register() {
        LOG_ITEM_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.itemlogger.log",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category.itemlogger"
        ));
    }
}