/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Block
/*    */   implements ClipContext.ShapeGetter
/*    */ {
/* 57 */   COLLIDER(BlockBehaviour.BlockStateBase::getCollisionShape),
/* 58 */   OUTLINE(BlockBehaviour.BlockStateBase::getShape),
/* 59 */   VISUAL(BlockBehaviour.BlockStateBase::getVisualShape),
/* 60 */   FALLDAMAGE_RESETTING((state, level, pos, collisionContext) -> {
/* 61 */       if (state.is(BlockTags.FALL_DAMAGE_RESETTING)) {
/* 62 */         return Shapes.block();
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 69 */       if (collisionContext instanceof EntityCollisionContext) { EntityCollisionContext entityCollisionContext = (EntityCollisionContext)collisionContext;
/* 70 */         if (entityCollisionContext.getEntity() != null && entityCollisionContext.getEntity().getType() == EntityType.PLAYER) {
/* 71 */           if (state.is(Blocks.END_GATEWAY) || state.is(Blocks.END_PORTAL)) {
/* 72 */             return Shapes.block();
/*    */           }
/* 74 */           if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (state
/* 75 */               .is(Blocks.NETHER_PORTAL) && ((Integer)serverLevel
/* 76 */               .getGameRules().get(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue() == 0)
/* 77 */               return Shapes.block();  }
/*    */         
/*    */         }  }
/*    */       
/* 81 */       return Shapes.empty();
/*    */     });
/*    */ 
/*    */   
/*    */   private final ClipContext.ShapeGetter shapeGetter;
/*    */ 
/*    */   
/* 88 */   Block(ClipContext.ShapeGetter getShape) { this.shapeGetter = getShape; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   public VoxelShape get(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return this.shapeGetter.get(state, level, pos, context); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ClipContext$Block.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */