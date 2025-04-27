package com.trongthang.weaponlevelingtransfer.mixin;

import com.trongthang.weaponlevelingtransfer.ConfigLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilTransferMixin {
    @Shadow private Property levelCost;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void improvedTransfer(CallbackInfo ci) {
        AnvilScreenHandler handler = (AnvilScreenHandler) (Object) this;
        ItemStack base = handler.getSlot(0).getStack();
        ItemStack sacrifice = handler.getSlot(1).getStack();

        // 1. Check item validity
        if (!isValidForTransfer(base) || !isValidForTransfer(sacrifice)) return;

        if(base.getNbt().getInt("level") == ConfigLoader.getInstance().maxLevel || sacrifice.getNbt().getInt("level") == ConfigLoader.getInstance().maxLevel){
            return;
        }
        // 2. Calculate combined XP
        int totalXP = getTotalXP(base) + getTotalXP(sacrifice);

        // 3. Calculate new level & progress
        int[] levelProgress = calculateLevelProgress(totalXP);
        int newLevel = levelProgress[0];
        int newProgress = levelProgress[1];

        // 4. Create result item
        ItemStack result = base.copy();
        NbtCompound resultNbt = result.getOrCreateNbt();
        resultNbt.putInt("level", newLevel);
        resultNbt.putInt("levelprogress", newProgress);

        // 5. Calculate cost
        int cost = calculateCost(newLevel, base.getNbt().getInt("level"));

        // 6. Update anvil
        handler.getSlot(2).setStack(result);
        this.levelCost.set(cost);
        ci.cancel();
    }

    private boolean isValidForTransfer(ItemStack stack) {
        // Check damage and NBT
        if (stack.isDamageable() && stack.isDamaged()) return false;
        return hasLevelNbt(stack);
    }

    private boolean hasLevelNbt(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.contains("level", NbtElement.NUMBER_TYPE);
    }

    private int getTotalXP(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        int level = nbt.getInt("level");
        int progress = nbt.contains("levelprogress", NbtElement.NUMBER_TYPE)
                ? nbt.getInt("levelprogress")
                : 0;
        return calculateXP(level) + progress;
    }

    private int calculateXP(int level) {
        // Formula: level*START_XP + (level*(level-1))/2
        return level * ConfigLoader.getInstance().startXP + (level * (level - 1)) / 2;
    }

    private int[] calculateLevelProgress(int totalXP) {
        int level = 0;
        int progress = 0;
        int currentXP = totalXP;

        while (level < ConfigLoader.getInstance().maxLevel) {
            int xpNeeded = ConfigLoader.getInstance().startXP + level * ConfigLoader.getInstance().xpIncrementPerLevel;
            if (currentXP >= xpNeeded) {
                currentXP -= xpNeeded;
                level++;
            } else {
                progress = currentXP;
                break;
            }
        }

        if (level >= ConfigLoader.getInstance().maxLevel) {
            level = ConfigLoader.getInstance().maxLevel;
            progress = 0;
        }

        return new int[]{level, progress};
    }

    private int calculateCost(int newLevel, int originalLevel) {
        int levelDifference = newLevel - originalLevel;
        if (levelDifference <= 0) return 3;

        double baseCost = Math.sqrt(levelDifference) * 4.5;
        return (int) Math.min(Math.max(Math.round(baseCost), 3), 100);
    }
}