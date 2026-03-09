/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EndCrystal
/*     */   extends Entity
/*     */ {
/*  24 */   private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*  25 */   private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_SHOW_BOTTOM = true;
/*     */   public int time;
/*     */   
/*     */   public EndCrystal(EntityType<? extends EndCrystal> type, Level level) {
/*  31 */     super(type, level);
/*  32 */     this.blocksBuilding = true;
/*     */     
/*  34 */     this.time = this.random.nextInt(100000);
/*     */   }
/*     */   
/*     */   public EndCrystal(Level level, double x, double y, double z) {
/*  38 */     this(EntityType.END_CRYSTAL, level);
/*  39 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  44 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  49 */     entityData.define(DATA_BEAM_TARGET, Optional.empty());
/*  50 */     entityData.define(DATA_SHOW_BOTTOM, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     this.time++;
/*  56 */     applyEffectsFromBlocks();
/*  57 */     handlePortal();
/*     */     
/*  59 */     if (level() instanceof ServerLevel) {
/*  60 */       BlockPos pos = blockPosition();
/*  61 */       if (((ServerLevel)level()).getDragonFight() != null && level().getBlockState(pos).isAir()) {
/*  62 */         level().setBlockAndUpdate(pos, BaseFireBlock.getState(level(), pos));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  69 */     output.storeNullable("beam_target", BlockPos.CODEC, getBeamTarget());
/*  70 */     output.putBoolean("ShowBottom", showsBottom());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  75 */     setBeamTarget((BlockPos)input.read("beam_target", BlockPos.CODEC).orElse(null));
/*  76 */     setShowBottom(input.getBooleanOr("ShowBottom", true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isPickable() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hurtClient(DamageSource source) {
/*  86 */     if (isInvulnerableToBase(source)) {
/*  87 */       return false;
/*     */     }
/*  89 */     if (source.getEntity() instanceof EnderDragon) {
/*  90 */       return false;
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  97 */     if (isInvulnerableToBase(source)) {
/*  98 */       return false;
/*     */     }
/* 100 */     if (source.getEntity() instanceof EnderDragon) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (!isRemoved()) {
/* 104 */       remove(Entity.RemovalReason.KILLED);
/*     */       
/* 106 */       if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
/* 107 */         DamageSource damageSource = (source.getEntity() != null) ? damageSources().explosion(this, source.getEntity()) : null;
/* 108 */         level.explode(this, damageSource, null, getX(), getY(), getZ(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
/*     */       } 
/*     */       
/* 111 */       onDestroyedBy(level, source);
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 118 */     onDestroyedBy(level, damageSources().generic());
/* 119 */     super.kill(level);
/*     */   }
/*     */   
/*     */   private void onDestroyedBy(ServerLevel level, DamageSource source) {
/* 123 */     EndDragonFight fight = level.getDragonFight();
/* 124 */     if (fight != null) {
/* 125 */       fight.onCrystalDestroyed(this, source);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 130 */   public void setBeamTarget(BlockPos target) { getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(target)); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public BlockPos getBeamTarget() { return (BlockPos)((Optional)getEntityData().get(DATA_BEAM_TARGET)).orElse(null); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public void setShowBottom(boolean showBottom) { getEntityData().set(DATA_SHOW_BOTTOM, Boolean.valueOf(showBottom)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public boolean showsBottom() { return ((Boolean)getEntityData().get(DATA_SHOW_BOTTOM)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public boolean shouldRenderAtSqrDistance(double distance) { return (super.shouldRenderAtSqrDistance(distance) || getBeamTarget() != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public ItemStack getPickResult() { return new ItemStack(Items.END_CRYSTAL); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\EndCrystal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */