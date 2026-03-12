/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPlatformFeature;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EndPortalBlock
/*     */   extends BaseEntityBlock implements Portal {
/*  34 */   public static final MapCodec<EndPortalBlock> CODEC = simpleCodec(EndPortalBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  38 */   public MapCodec<EndPortalBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 6.0D, 12.0D);
/*     */ 
/*     */   
/*  44 */   protected EndPortalBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new TheEndPortalBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) { return state.getShape(level, pos); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  64 */     if (entity.canUsePortal(false)) {
/*  65 */       if (!level.isClientSide() && level.dimension() == Level.END && entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity; if (!player.seenCredits) {
/*  66 */           player.showEndCredits(); return;
/*     */         }  }
/*  68 */        entity.setAsInsidePortal(this, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
/*     */     Set<Relative> relatives;
/*     */     float xRot, yRot;
/*  75 */     LevelData.RespawnData respawnData = currentLevel.getRespawnData();
/*  76 */     ResourceKey<Level> currentDimension = currentLevel.dimension();
/*  77 */     boolean fromEnd = (currentDimension == Level.END);
/*  78 */     ResourceKey<Level> newDimension = fromEnd ? respawnData.dimension() : Level.END;
/*  79 */     BlockPos spawnBlockPos = fromEnd ? respawnData.pos() : ServerLevel.END_SPAWN_POINT;
/*  80 */     ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
/*  81 */     if (newLevel == null) {
/*  82 */       return null;
/*     */     }
/*     */     
/*  85 */     Vec3 spawnPos = spawnBlockPos.getBottomCenter();
/*     */ 
/*     */ 
/*     */     
/*  89 */     if (!fromEnd) {
/*  90 */       EndPlatformFeature.createEndPlatform(newLevel, BlockPos.containing(spawnPos).below(), true);
/*  91 */       yRot = Direction.WEST.toYRot();
/*  92 */       xRot = 0.0F;
/*  93 */       relatives = Relative.union(new Set[] { null, (new Set[2][0] = Relative.DELTA).of(Relative.X_ROT) });
/*  94 */       if (entity instanceof ServerPlayer) {
/*  95 */         spawnPos = spawnPos.subtract(0.0D, 1.0D, 0.0D);
/*     */       }
/*     */     } else {
/*  98 */       yRot = respawnData.yaw();
/*  99 */       xRot = respawnData.pitch();
/* 100 */       relatives = Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION });
/* 101 */       if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 102 */         return serverPlayer.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING); }
/*     */       
/* 104 */       spawnPos = entity.adjustSpawnLocation(newLevel, spawnBlockPos).getBottomCenter();
/*     */     } 
/*     */ 
/*     */     
/* 108 */     return new TeleportTransition(newLevel, spawnPos, Vec3.ZERO, yRot, xRot, relatives, TeleportTransition.PLAY_PORTAL_SOUND
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 115 */         .then(TeleportTransition.PLACE_PORTAL_TICKET));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 121 */     double x = pos.getX() + random.nextDouble();
/* 122 */     double y = pos.getY() + 0.8D;
/* 123 */     double z = pos.getZ() + random.nextDouble();
/*     */     
/* 125 */     level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   protected boolean canBeReplaced(BlockState state, Fluid fluid) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EndPortalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */