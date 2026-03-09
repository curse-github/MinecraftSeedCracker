/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NodeEvaluator
/*     */ {
/*     */   protected PathfindingContext currentContext;
/*     */   protected Mob mob;
/*  22 */   protected final Int2ObjectMap<Node> nodes = new Int2ObjectOpenHashMap();
/*     */   
/*     */   protected int entityWidth;
/*     */   
/*     */   protected int entityHeight;
/*     */   
/*     */   protected int entityDepth;
/*     */   
/*     */   protected boolean canPassDoors = true;
/*     */   protected boolean canOpenDoors;
/*     */   protected boolean canFloat;
/*     */   protected boolean canWalkOverFences;
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  36 */     this.currentContext = new PathfindingContext(level, entity);
/*  37 */     this.mob = entity;
/*  38 */     this.nodes.clear();
/*     */     
/*  40 */     this.entityWidth = Mth.floor(entity.getBbWidth() + 1.0F);
/*  41 */     this.entityHeight = Mth.floor(entity.getBbHeight() + 1.0F);
/*  42 */     this.entityDepth = Mth.floor(entity.getBbWidth() + 1.0F);
/*     */   }
/*     */   
/*     */   public void done() {
/*  46 */     this.currentContext = null;
/*  47 */     this.mob = null;
/*     */   }
/*     */ 
/*     */   
/*  51 */   protected Node getNode(BlockPos pos) { return getNode(pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected Node getNode(int x, int y, int z) { return (Node)this.nodes.computeIfAbsent(Node.createHash(x, y, z), k -> new Node(x, y, z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected Target getTargetNodeAt(double x, double y, double z) { return new Target(getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public PathType getPathType(Mob mob, BlockPos pos) { return getPathType(new PathfindingContext(mob.level(), mob), pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public void setCanPassDoors(boolean canPassDoors) { this.canPassDoors = canPassDoors; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public void setCanOpenDoors(boolean canOpenDoors) { this.canOpenDoors = canOpenDoors; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void setCanFloat(boolean canFloat) { this.canFloat = canFloat; }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public void setCanWalkOverFences(boolean canWalkOverFences) { this.canWalkOverFences = canWalkOverFences; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean canPassDoors() { return this.canPassDoors; }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean canOpenDoors() { return this.canOpenDoors; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean canFloat() { return this.canFloat; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean canWalkOverFences() { return this.canWalkOverFences; }
/*     */ 
/*     */   
/*     */   public static boolean isBurningBlock(BlockState blockState) {
/* 109 */     return (blockState.is(BlockTags.FIRE) || blockState
/* 110 */       .is(Blocks.LAVA) || blockState
/* 111 */       .is(Blocks.MAGMA_BLOCK) || 
/* 112 */       CampfireBlock.isLitCampfire(blockState) || blockState
/* 113 */       .is(Blocks.LAVA_CAULDRON));
/*     */   }
/*     */   
/*     */   public abstract Node getStart();
/*     */   
/*     */   public abstract Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract int getNeighbors(Node[] paramArrayOfNode, Node paramNode);
/*     */   
/*     */   public abstract PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob);
/*     */   
/*     */   public abstract PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\NodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */