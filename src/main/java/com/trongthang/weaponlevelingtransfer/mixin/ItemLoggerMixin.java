package com.trongthang.weaponlevelingtransfer.mixin;

import com.trongthang.weaponlevelingtransfer.ModKeyBinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ItemLoggerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        if (ModKeyBinds.LOG_ITEM_KEY.wasPressed()) {
            ItemStack stack = player.getMainHandStack();
            if (!stack.isEmpty()) {
                NbtCompound nbt = stack.getNbt();
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                        Text.literal("Item NBT: " + (nbt != null ? nbt.toString() : "No NBT Data"))
                );
                // Log to console for deeper inspection
                System.out.println("Full Item NBT: " + nbt);
            }
        }
    }
}
