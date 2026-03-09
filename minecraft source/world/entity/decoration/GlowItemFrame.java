/*    */ package net.minecraft.world.entity.decoration;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class GlowItemFrame
/*    */   extends ItemFrame {
/* 14 */   public GlowItemFrame(EntityType<? extends ItemFrame> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public GlowItemFrame(Level level, BlockPos pos, Direction direction) { super(EntityType.GLOW_ITEM_FRAME, level, pos, direction); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public SoundEvent getRemoveItemSound() { return SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public SoundEvent getBreakSound() { return SoundEvents.GLOW_ITEM_FRAME_BREAK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public SoundEvent getPlaceSound() { return SoundEvents.GLOW_ITEM_FRAME_PLACE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public SoundEvent getAddItemSound() { return SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public SoundEvent getRotateItemSound() { return SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected ItemStack getFrameItemStack() { return new ItemStack(Items.GLOW_ITEM_FRAME); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\GlowItemFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */