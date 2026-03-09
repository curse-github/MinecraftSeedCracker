/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ 
/*    */ public interface SignApplicator
/*    */ {
/*    */   boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, Player paramPlayer);
/*    */   
/* 13 */   default boolean canApplyToSign(SignText text, Player player) { return text.hasMessage(player); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SignApplicator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */