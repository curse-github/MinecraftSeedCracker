/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Cursor3D;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class BlockCollisions<T>
/*     */   extends AbstractIterator<T>
/*     */ {
/*     */   private final AABB box;
/*     */   private final CollisionContext context;
/*     */   private final Cursor3D cursor;
/*     */   private final BlockPos.MutableBlockPos pos;
/*     */   private final VoxelShape entityShape;
/*     */   private final CollisionGetter collisionGetter;
/*     */   private final boolean onlySuffocatingBlocks;
/*     */   private BlockGetter cachedBlockGetter;
/*     */   private long cachedBlockGetterPos;
/*     */   private final BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider;
/*     */   
/*     */   public BlockCollisions(CollisionGetter collisionGetter, Entity source, AABB box, boolean onlySuffocatingBlocks, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider) {
/*  34 */     this(collisionGetter, 
/*     */         
/*  36 */         (source == null) ? CollisionContext.empty() : CollisionContext.of(source), box, onlySuffocatingBlocks, resultProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockCollisions(CollisionGetter collisionGetter, CollisionContext context, AABB box, boolean onlySuffocatingBlocks, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider) {
/*  44 */     this.context = context;
/*  45 */     this.pos = new BlockPos.MutableBlockPos();
/*  46 */     this.entityShape = Shapes.create(box);
/*  47 */     this.collisionGetter = collisionGetter;
/*  48 */     this.box = box;
/*  49 */     this.onlySuffocatingBlocks = onlySuffocatingBlocks;
/*  50 */     this.resultProvider = resultProvider;
/*     */ 
/*     */     
/*  53 */     int x0 = Mth.floor(box.minX - 1.0E-7D) - 1;
/*  54 */     int x1 = Mth.floor(box.maxX + 1.0E-7D) + 1;
/*  55 */     int y0 = Mth.floor(box.minY - 1.0E-7D) - 1;
/*  56 */     int y1 = Mth.floor(box.maxY + 1.0E-7D) + 1;
/*  57 */     int z0 = Mth.floor(box.minZ - 1.0E-7D) - 1;
/*  58 */     int z1 = Mth.floor(box.maxZ + 1.0E-7D) + 1;
/*  59 */     this.cursor = new Cursor3D(x0, y0, z0, x1, y1, z1);
/*     */   }
/*     */   
/*     */   private BlockGetter getChunk(int x, int z) {
/*  63 */     int chunkX = SectionPos.blockToSectionCoord(x);
/*  64 */     int chunkZ = SectionPos.blockToSectionCoord(z);
/*     */     
/*  66 */     long chunkPos = ChunkPos.asLong(chunkX, chunkZ);
/*  67 */     if (this.cachedBlockGetter != null && this.cachedBlockGetterPos == chunkPos) {
/*  68 */       return this.cachedBlockGetter;
/*     */     }
/*  70 */     BlockGetter result = this.collisionGetter.getChunkForCollisions(chunkX, chunkZ);
/*  71 */     this.cachedBlockGetter = result;
/*  72 */     this.cachedBlockGetterPos = chunkPos;
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected T computeNext() {
/*  78 */     while (this.cursor.advance()) {
/*  79 */       int x = this.cursor.nextX();
/*  80 */       int y = this.cursor.nextY();
/*  81 */       int z = this.cursor.nextZ();
/*     */       
/*  83 */       int cursorFaceType = this.cursor.getNextType();
/*     */       
/*  85 */       if (cursorFaceType == 3) {
/*     */         continue;
/*     */       }
/*     */       
/*  89 */       BlockGetter chunk = getChunk(x, z);
/*     */       
/*  91 */       if (chunk == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  95 */       this.pos.set(x, y, z);
/*  96 */       BlockState blockState = chunk.getBlockState(this.pos);
/*     */       
/*  98 */       if (this.onlySuffocatingBlocks && !blockState.isSuffocating(chunk, this.pos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 102 */       if (cursorFaceType == 1 && !blockState.hasLargeCollisionShape()) {
/*     */         continue;
/*     */       }
/* 105 */       if (cursorFaceType == 2 && !blockState.is(Blocks.MOVING_PISTON)) {
/*     */         continue;
/*     */       }
/*     */       
/* 109 */       VoxelShape blockShape = this.context.getCollisionShape(blockState, this.collisionGetter, this.pos);
/*     */       
/* 111 */       if (blockShape == Shapes.block()) {
/* 112 */         if (this.box.intersects(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D))
/* 113 */           return (T)this.resultProvider.apply(this.pos, blockShape.move(this.pos)); 
/*     */         continue;
/*     */       } 
/* 116 */       VoxelShape shape = blockShape.move(this.pos);
/* 117 */       if (!shape.isEmpty() && Shapes.joinIsNotEmpty(shape, this.entityShape, BooleanOp.AND)) {
/* 118 */         return (T)this.resultProvider.apply(this.pos, shape);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return (T)endOfData();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BlockCollisions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */