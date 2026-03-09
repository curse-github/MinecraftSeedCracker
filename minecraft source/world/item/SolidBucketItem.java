/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SolidBucketItem
/*    */   extends BlockItem implements DispensibleContainerItem {
/*    */   private final SoundEvent placeSound;
/*    */   
/*    */   public SolidBucketItem(Block content, SoundEvent placeSound, Item.Properties properties) {
/* 21 */     super(content, properties);
/* 22 */     this.placeSound = placeSound;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 27 */     InteractionResult placeResult = super.useOn(context);
/* 28 */     Player player = context.getPlayer();
/*    */     
/* 30 */     if (placeResult.consumesAction() && player != null) {
/* 31 */       player.setItemInHand(context.getHand(), BucketItem.getEmptySuccessItem(context.getItemInHand(), player));
/*    */     }
/*    */     
/* 34 */     return placeResult;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected SoundEvent getPlaceSound(BlockState blockState) { return this.placeSound; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean emptyContents(LivingEntity user, Level level, BlockPos pos, BlockHitResult hitResult) {
/* 44 */     if (level.isInWorldBounds(pos) && level.isEmptyBlock(pos)) {
/* 45 */       if (!level.isClientSide()) {
/* 46 */         level.setBlock(pos, getBlock().defaultBlockState(), 3);
/*    */       }
/* 48 */       level.gameEvent(user, GameEvent.FLUID_PLACE, pos);
/* 49 */       level.playSound(user, pos, this.placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SolidBucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */