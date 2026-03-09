/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ClipContext
/*     */ {
/*     */   private final Vec3 from;
/*     */   private final Vec3 to;
/*     */   private final Block block;
/*     */   private final Fluid fluid;
/*     */   private final CollisionContext collisionContext;
/*     */   
/*  29 */   public ClipContext(Vec3 from, Vec3 to, Block block, Fluid fluid, Entity entity) { this(from, to, block, fluid, CollisionContext.of(entity)); }
/*     */ 
/*     */   
/*     */   public ClipContext(Vec3 from, Vec3 to, Block block, Fluid fluid, CollisionContext collisionContext) {
/*  33 */     this.from = from;
/*  34 */     this.to = to;
/*  35 */     this.block = block;
/*  36 */     this.fluid = fluid;
/*  37 */     this.collisionContext = collisionContext;
/*     */   }
/*     */ 
/*     */   
/*  41 */   public Vec3 getTo() { return this.to; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public Vec3 getFrom() { return this.from; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public VoxelShape getBlockShape(BlockState blockState, BlockGetter level, BlockPos pos) { return this.block.get(blockState, level, pos, this.collisionContext); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public VoxelShape getFluidShape(FluidState fluidState, BlockGetter level, BlockPos pos) { return this.fluid.canPick(fluidState) ? fluidState.getShape(level, pos) : Shapes.empty(); }
/*     */   
/*     */   public enum Block
/*     */     implements ShapeGetter {
/*  57 */     COLLIDER(BlockBehaviour.BlockStateBase::getCollisionShape),
/*  58 */     OUTLINE(BlockBehaviour.BlockStateBase::getShape),
/*  59 */     VISUAL(BlockBehaviour.BlockStateBase::getVisualShape),
/*  60 */     FALLDAMAGE_RESETTING((state, level, pos, collisionContext) -> {
/*  61 */         if (state.is(BlockTags.FALL_DAMAGE_RESETTING)) {
/*  62 */           return Shapes.block();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  69 */         if (collisionContext instanceof EntityCollisionContext) { EntityCollisionContext entityCollisionContext = (EntityCollisionContext)collisionContext;
/*  70 */           if (entityCollisionContext.getEntity() != null && entityCollisionContext.getEntity().getType() == EntityType.PLAYER) {
/*  71 */             if (state.is(Blocks.END_GATEWAY) || state.is(Blocks.END_PORTAL)) {
/*  72 */               return Shapes.block();
/*     */             }
/*  74 */             if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (state
/*  75 */                 .is(Blocks.NETHER_PORTAL) && ((Integer)serverLevel
/*  76 */                 .getGameRules().get(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue() == 0)
/*  77 */                 return Shapes.block();  }
/*     */           
/*     */           }  }
/*     */         
/*  81 */         return Shapes.empty();
/*     */       });
/*     */ 
/*     */     
/*     */     private final ClipContext.ShapeGetter shapeGetter;
/*     */ 
/*     */     
/*  88 */     Block(ClipContext.ShapeGetter getShape) { this.shapeGetter = getShape; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     public VoxelShape get(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return this.shapeGetter.get(state, level, pos, context); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Fluid
/*     */   {
/* 102 */     NONE(state -> false),
/* 103 */     SOURCE_ONLY(FluidState::isSource),
/* 104 */     ANY(state -> !state.isEmpty()),
/* 105 */     WATER(fluidState -> fluidState.is(FluidTags.WATER));
/*     */ 
/*     */     
/*     */     private final Predicate<FluidState> canPick;
/*     */ 
/*     */     
/* 111 */     Fluid(Predicate<FluidState> canPick) { this.canPick = canPick; }
/*     */ 
/*     */ 
/*     */     
/* 115 */     public boolean canPick(FluidState fluidState) { return this.canPick.test(fluidState); }
/*     */   }
/*     */   
/*     */   public static interface ShapeGetter {
/*     */     VoxelShape get(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, CollisionContext param1CollisionContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ClipContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */