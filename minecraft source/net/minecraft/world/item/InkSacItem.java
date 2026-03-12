/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class InkSacItem extends Item implements SignApplicator {
/* 11 */   public InkSacItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, Player player) {
/* 16 */     if (sign.updateText(text -> text.setHasGlowingText(false), isFrontText)) {
/* 17 */       level.playSound(null, sign.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\InkSacItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */