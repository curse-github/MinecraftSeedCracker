/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PowderSnowBlock
/*     */   extends Block
/*     */   implements BucketPickup {
/*  38 */   public static final MapCodec<PowderSnowBlock> CODEC = simpleCodec(PowderSnowBlock::new);
/*     */   private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336F;
/*     */   private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;
/*     */   
/*  42 */   public MapCodec<PowderSnowBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;
/*     */   
/*     */   private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
/*     */   
/*  50 */   private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.8999999761581421D, 1.0D);
/*     */   
/*     */   private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0D;
/*     */   private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0D;
/*     */   
/*  55 */   public PowderSnowBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/*  60 */     if (neighborState.is(this)) {
/*  61 */       return true;
/*     */     }
/*  63 */     return super.skipRendering(state, neighborState, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  68 */     if (!(entity instanceof LivingEntity) || entity.getInBlockState().is(this)) {
/*  69 */       entity.makeStuckInBlock(state, new Vec3(0.8999999761581421D, 1.5D, 0.8999999761581421D));
/*     */       
/*  71 */       if (level.isClientSide()) {
/*  72 */         RandomSource random = level.getRandom();
/*  73 */         boolean isMoving = (entity.xOld != entity.getX() || entity.zOld != entity.getZ());
/*     */         
/*  75 */         if (isMoving && random.nextBoolean()) {
/*  76 */           level.addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), (pos.getY() + 1), entity.getZ(), (Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806D, (Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     BlockPos position = pos.immutable();
/*  82 */     effectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, e -> {
/*  83 */           if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  84 */             if (e.isOnFire() && (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || e instanceof net.minecraft.world.entity.player.Player) && e.mayInteract(serverLevel, position)) {
/*  85 */               level.destroyBlock(position, false);
/*     */             } }
/*     */         
/*     */         });
/*  89 */     effectApplier.apply(InsideBlockEffectType.FREEZE);
/*  90 */     effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/*     */     LivingEntity livingEntity;
/*  96 */     if (fallDistance >= 4.0D && entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 100 */     LivingEntity.Fallsounds entityFallsounds = livingEntity.getFallSounds();
/* 101 */     SoundEvent fallSound = (fallDistance < 7.0D) ? entityFallsounds.small() : entityFallsounds.big();
/*     */     
/* 103 */     entity.playSound(fallSound, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
/* 108 */     VoxelShape collisionShape = getCollisionShape(state, level, pos, CollisionContext.of(entity));
/* 109 */     return collisionShape.isEmpty() ? Shapes.block() : collisionShape;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 114 */     if (!context.isPlacement() && context instanceof EntityCollisionContext) { EntityCollisionContext entityCollisionContext = (EntityCollisionContext)context;
/* 115 */       Entity entity = entityCollisionContext.getEntity();
/* 116 */       if (entity != null) {
/* 117 */         if (entity.fallDistance > 2.5D) {
/* 118 */           return FALLING_COLLISION_SHAPE;
/*     */         }
/*     */         
/* 121 */         boolean isFallingBlock = entity instanceof net.minecraft.world.entity.item.FallingBlockEntity;
/* 122 */         if (isFallingBlock || (canEntityWalkOnPowderSnow(entity) && context.isAbove(Shapes.block(), pos, false) && !context.isDescending())) {
/* 123 */           return super.getCollisionShape(state, level, pos, context);
/*     */         }
/*     */       }  }
/*     */     
/* 127 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
/*     */ 
/*     */   
/*     */   public static boolean canEntityWalkOnPowderSnow(Entity entity) {
/* 136 */     if (entity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
/* 137 */       return true;
/*     */     }
/*     */     
/* 140 */     if (entity instanceof LivingEntity) {
/* 141 */       return ((LivingEntity)entity).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
/*     */     }
/*     */     
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 149 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 150 */     if (!level.isClientSide()) {
/* 151 */       level.levelEvent(2001, pos, Block.getId(state));
/*     */     }
/* 153 */     return new ItemStack(Items.POWDER_SNOW_BUCKET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public Optional<SoundEvent> getPickupSound() { return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PowderSnowBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */