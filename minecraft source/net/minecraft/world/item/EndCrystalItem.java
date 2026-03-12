/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class EndCrystalItem
/*    */   extends Item
/*    */ {
/* 20 */   public EndCrystalItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 25 */     Level level = context.getLevel();
/* 26 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 28 */     BlockState blockState = level.getBlockState(pos);
/* 29 */     if (!blockState.is(Blocks.OBSIDIAN) && !blockState.is(Blocks.BEDROCK)) {
/* 30 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 33 */     BlockPos above = pos.above();
/* 34 */     if (!level.isEmptyBlock(above)) {
/* 35 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 38 */     double x = above.getX();
/* 39 */     double y = above.getY();
/* 40 */     double z = above.getZ();
/*    */     
/* 42 */     List<Entity> entities = level.getEntities(null, new AABB(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));
/* 43 */     if (!entities.isEmpty()) {
/* 44 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 47 */     if (level instanceof ServerLevel) {
/* 48 */       EndCrystal crystal = new EndCrystal(level, x + 0.5D, y, z + 0.5D);
/* 49 */       crystal.setShowBottom(false);
/* 50 */       level.addFreshEntity(crystal);
/* 51 */       level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, above);
/*    */       
/* 53 */       EndDragonFight fight = ((ServerLevel)level).getDragonFight();
/*    */       
/* 55 */       if (fight != null) {
/* 56 */         fight.tryRespawn();
/*    */       }
/*    */     } 
/* 59 */     context.getItemInHand().shrink(1);
/* 60 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\EndCrystalItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */