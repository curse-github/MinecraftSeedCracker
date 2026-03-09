/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HangingSignBlockEntity
/*    */   extends SignBlockEntity {
/*    */   private static final int MAX_TEXT_LINE_WIDTH = 60;
/*    */   private static final int TEXT_LINE_HEIGHT = 9;
/*    */   
/* 13 */   public HangingSignBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.HANGING_SIGN, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public int getTextLineHeight() { return 9; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public int getMaxTextLineWidth() { return 60; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public SoundEvent getSignInteractionFailedSoundEvent() { return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\HangingSignBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */