/*    */ package net.minecraft.core.dispenser;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.Shearable;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.BeehiveBlock;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class ShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 24 */     ServerLevel level = source.level();
/* 25 */     if (!level.isClientSide()) {
/* 26 */       BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*    */       
/* 28 */       setSuccess((tryShearBeehive(level, dispensed, pos) || tryShearEntity(level, pos, dispensed)));
/* 29 */       if (isSuccess())
/* 30 */         dispensed.hurtAndBreak(1, level, null, item -> {
/*    */             
/*    */             }); 
/* 33 */     }  return dispensed;
/*    */   }
/*    */   
/*    */   private static boolean tryShearBeehive(ServerLevel level, ItemStack tool, BlockPos pos) {
/* 37 */     BlockState state = level.getBlockState(pos);
/* 38 */     if (state.is(BlockTags.BEEHIVES, s -> (s.hasProperty(BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock))) {
/* 39 */       int honeyLevel = ((Integer)state.getValue(BeehiveBlock.HONEY_LEVEL)).intValue();
/*    */       
/* 41 */       if (honeyLevel >= 5) {
/* 42 */         level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */         
/* 44 */         BeehiveBlock.dropHoneycomb(level, tool, state, level.getBlockEntity(pos), null, pos);
/* 45 */         ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel(level, state, pos, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 46 */         level.gameEvent(null, GameEvent.SHEAR, pos);
/* 47 */         return true;
/*    */       } 
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean tryShearEntity(ServerLevel level, BlockPos pos, ItemStack tool) {
/* 54 */     List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
/* 55 */     for (Entity entity : entities) {
/* 56 */       if (entity.shearOffAllLeashConnections(null)) {
/* 57 */         return true;
/*    */       }
/* 59 */       if (entity instanceof Shearable) { Shearable shearable = (Shearable)entity;
/* 60 */         if (shearable.readyForShearing()) {
/* 61 */           shearable.shear(level, SoundSource.BLOCKS, tool);
/* 62 */           level.gameEvent(null, GameEvent.SHEAR, pos);
/* 63 */           return true;
/*    */         }  }
/*    */     
/*    */     } 
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\ShearsDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */