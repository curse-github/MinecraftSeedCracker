/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BellBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int DURATION = 50;
/*     */   private static final int GLOW_DURATION = 60;
/*     */   private static final int MIN_TICKS_BETWEEN_SEARCHES = 60;
/*     */   private static final int MAX_RESONATION_TICKS = 40;
/*     */   private static final int TICKS_BEFORE_RESONATION = 5;
/*     */   private static final int SEARCH_RADIUS = 48;
/*     */   private static final int HEAR_BELL_RADIUS = 32;
/*     */   private static final int HIGHLIGHT_RAIDERS_RADIUS = 48;
/*     */   private long lastRingTimestamp;
/*     */   public int ticks;
/*     */   public boolean shaking;
/*     */   public Direction clickDirection;
/*     */   private List<LivingEntity> nearbyEntities;
/*     */   private boolean resonating;
/*     */   private int resonationTicks;
/*     */   
/*  43 */   public BellBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BELL, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int b0, int b1) {
/*  48 */     if (b0 == 1) {
/*  49 */       updateEntities();
/*  50 */       this.resonationTicks = 0;
/*  51 */       this.clickDirection = Direction.from3DDataValue(b1);
/*  52 */       this.ticks = 0;
/*  53 */       this.shaking = true;
/*  54 */       return true;
/*     */     } 
/*  56 */     return super.triggerEvent(b0, b1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void tick(Level level, BlockPos pos, BlockState state, BellBlockEntity entity, ResonationEndAction onResonationEnd) {
/*  65 */     if (entity.shaking) {
/*  66 */       entity.ticks++;
/*     */     }
/*     */     
/*  69 */     if (entity.ticks >= 50) {
/*  70 */       entity.shaking = false;
/*  71 */       entity.ticks = 0;
/*     */     } 
/*     */     
/*  74 */     if (entity.ticks >= 5 && entity.resonationTicks == 0 && areRaidersNearby(pos, entity.nearbyEntities)) {
/*  75 */       entity.resonating = true;
/*  76 */       level.playSound(null, pos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */     
/*  79 */     if (entity.resonating) {
/*  80 */       if (entity.resonationTicks < 40) {
/*  81 */         entity.resonationTicks++;
/*     */       } else {
/*  83 */         onResonationEnd.run(level, pos, entity.nearbyEntities);
/*  84 */         entity.resonating = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  90 */   public static void clientTick(Level level, BlockPos pos, BlockState state, BellBlockEntity entity) { tick(level, pos, state, entity, BellBlockEntity::showBellParticles); }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static void serverTick(Level level, BlockPos pos, BlockState state, BellBlockEntity entity) { tick(level, pos, state, entity, BellBlockEntity::makeRaidersGlow); }
/*     */ 
/*     */   
/*     */   public void onHit(Direction clickDirection) {
/*  98 */     BlockPos bellPos = getBlockPos();
/*     */     
/* 100 */     this.clickDirection = clickDirection;
/* 101 */     if (this.shaking) {
/* 102 */       this.ticks = 0;
/*     */     } else {
/* 104 */       this.shaking = true;
/*     */     } 
/*     */     
/* 107 */     this.level.blockEvent(bellPos, getBlockState().getBlock(), 1, clickDirection.get3DDataValue());
/*     */   }
/*     */   
/*     */   private void updateEntities() {
/* 111 */     BlockPos blockPos = getBlockPos();
/*     */     
/* 113 */     if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
/* 114 */       this.lastRingTimestamp = this.level.getGameTime();
/* 115 */       AABB aabb = (new AABB(blockPos)).inflate(48.0D);
/* 116 */       this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
/*     */     } 
/*     */     
/* 119 */     if (!this.level.isClientSide()) {
/* 120 */       for (LivingEntity entity : this.nearbyEntities) {
/* 121 */         if (!entity.isAlive() || entity.isRemoved()) {
/*     */           continue;
/*     */         }
/* 124 */         if (blockPos.closerToCenterThan(entity.position(), 32.0D)) {
/* 125 */           entity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, Long.valueOf(this.level.getGameTime()));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean areRaidersNearby(BlockPos bellPos, List<LivingEntity> nearbyEntities) {
/* 132 */     for (LivingEntity entity : nearbyEntities) {
/* 133 */       if (!entity.isAlive() || entity.isRemoved()) {
/*     */         continue;
/*     */       }
/* 136 */       if (bellPos.closerToCenterThan(entity.position(), 32.0D) && 
/* 137 */         entity.getType().is(EntityTypeTags.RAIDERS)) {
/* 138 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private static void makeRaidersGlow(Level level, BlockPos blockPos, List<LivingEntity> nearbyEntities) {
/* 146 */     nearbyEntities.stream()
/* 147 */       .filter(e -> isRaiderWithinRange(blockPos, e))
/* 148 */       .forEach(BellBlockEntity::glow);
/*     */   }
/*     */   
/*     */   private static void showBellParticles(Level level, BlockPos bellPos, List<LivingEntity> nearbyEntities) {
/* 152 */     MutableInt particleColor = new MutableInt(16700985);
/*     */     
/* 154 */     int nearbyRaiderCount = (int)nearbyEntities.stream().filter(p -> bellPos.closerToCenterThan(p.position(), 48.0D)).count();
/*     */     
/* 156 */     nearbyEntities.stream()
/* 157 */       .filter(e -> isRaiderWithinRange(bellPos, e))
/* 158 */       .forEach(entity -> {
/* 159 */           float distAway = 1.0F;
/* 160 */           double distBtwn = Math.sqrt((entity.getX() - bellPos.getX()) * (entity.getX() - bellPos.getX()) + (entity.getZ() - bellPos.getZ()) * (entity.getZ() - bellPos.getZ()));
/* 161 */           double x3 = (bellPos.getX() + 0.5F) + 1.0D / distBtwn * (entity.getX() - bellPos.getX());
/* 162 */           double z3 = (bellPos.getZ() + 0.5F) + 1.0D / distBtwn * (entity.getZ() - bellPos.getZ());
/*     */           
/* 164 */           int particleCount = Mth.clamp((nearbyRaiderCount - 21) / -2, 3, 15);
/* 165 */           for (int i = 0; i < particleCount; i++) {
/* 166 */             int color = particleColor.addAndGet(5);
/* 167 */             level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color), x3, (bellPos.getY() + 0.5F), z3, 0.0D, 0.0D, 0.0D);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isRaiderWithinRange(BlockPos blockPos, LivingEntity entity) {
/* 173 */     return (entity.isAlive() && 
/* 174 */       !entity.isRemoved() && blockPos
/* 175 */       .closerToCenterThan(entity.position(), 48.0D) && entity
/* 176 */       .getType().is(EntityTypeTags.RAIDERS));
/*     */   }
/*     */ 
/*     */   
/* 180 */   private static void glow(LivingEntity raider) { raider.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60)); }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ResonationEndAction {
/*     */     void run(Level param1Level, BlockPos param1BlockPos, List<LivingEntity> param1List);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BellBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */