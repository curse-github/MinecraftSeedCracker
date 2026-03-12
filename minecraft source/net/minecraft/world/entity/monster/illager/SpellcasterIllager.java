/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class SpellcasterIllager
/*     */   extends AbstractIllager
/*     */ {
/*  25 */   private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(SpellcasterIllager.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int DEFAULT_SPELLCASTING_TICKS = 0;
/*  28 */   protected int spellCastingTickCount = 0;
/*  29 */   private IllagerSpell currentSpell = IllagerSpell.NONE;
/*     */ 
/*     */   
/*  32 */   protected SpellcasterIllager(EntityType<? extends SpellcasterIllager> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  37 */     super.defineSynchedData(entityData);
/*     */     
/*  39 */     entityData.define(DATA_SPELL_CASTING_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  44 */     super.readAdditionalSaveData(input);
/*     */     
/*  46 */     this.spellCastingTickCount = input.getIntOr("SpellTicks", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  51 */     super.addAdditionalSaveData(output);
/*     */     
/*  53 */     output.putInt("SpellTicks", this.spellCastingTickCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/*  58 */     if (isCastingSpell())
/*  59 */       return AbstractIllager.IllagerArmPose.SPELLCASTING; 
/*  60 */     if (isCelebrating()) {
/*  61 */       return AbstractIllager.IllagerArmPose.CELEBRATING;
/*     */     }
/*  63 */     return AbstractIllager.IllagerArmPose.CROSSED;
/*     */   }
/*     */   
/*     */   public boolean isCastingSpell() {
/*  67 */     if (level().isClientSide()) {
/*  68 */       return (((Byte)this.entityData.get(DATA_SPELL_CASTING_ID)).byteValue() > 0);
/*     */     }
/*  70 */     return (this.spellCastingTickCount > 0);
/*     */   }
/*     */   
/*     */   public void setIsCastingSpell(IllagerSpell spell) {
/*  74 */     this.currentSpell = spell;
/*  75 */     this.entityData.set(DATA_SPELL_CASTING_ID, Byte.valueOf((byte)spell.id));
/*     */   }
/*     */   
/*     */   protected IllagerSpell getCurrentSpell() {
/*  79 */     if (!level().isClientSide()) {
/*  80 */       return this.currentSpell;
/*     */     }
/*  82 */     return IllagerSpell.byId(((Byte)this.entityData.get(DATA_SPELL_CASTING_ID)).byteValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  87 */     super.customServerAiStep(level);
/*     */     
/*  89 */     if (this.spellCastingTickCount > 0) {
/*  90 */       this.spellCastingTickCount--;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  96 */     super.tick();
/*     */     
/*  98 */     if (level().isClientSide() && isCastingSpell()) {
/*  99 */       IllagerSpell spell = getCurrentSpell();
/* 100 */       float red = (float)spell.spellColor[0];
/* 101 */       float green = (float)spell.spellColor[1];
/* 102 */       float blue = (float)spell.spellColor[2];
/*     */ 
/*     */       
/* 105 */       float bodyAngle = this.yBodyRot * 0.017453292F + Mth.cos((this.tickCount * 0.6662F)) * 0.25F;
/* 106 */       float cos = Mth.cos(bodyAngle);
/* 107 */       float sin = Mth.sin(bodyAngle);
/*     */       
/* 109 */       double handDistance = 0.6D * getScale();
/* 110 */       double handHeight = 1.8D * getScale();
/* 111 */       level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue), getX() + cos * handDistance, getY() + handHeight, getZ() + sin * handDistance, 0.0D, 0.0D, 0.0D);
/* 112 */       level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue), getX() - cos * handDistance, getY() + handHeight, getZ() - sin * handDistance, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 117 */   protected int getSpellCastingTime() { return this.spellCastingTickCount; }
/*     */   
/*     */   protected abstract SoundEvent getCastingSoundEvent();
/*     */   
/*     */   protected class SpellcasterCastingSpellGoal
/*     */     extends Goal
/*     */   {
/* 124 */     public SpellcasterCastingSpellGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     public boolean canUse() { return (SpellcasterIllager.this.getSpellCastingTime() > 0); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 134 */       super.start();
/* 135 */       SpellcasterIllager.this.navigation.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 140 */       super.stop();
/* 141 */       SpellcasterIllager.this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 146 */       if (SpellcasterIllager.this.getTarget() != null)
/* 147 */         SpellcasterIllager.this.getLookControl().setLookAt(SpellcasterIllager.this.getTarget(), SpellcasterIllager.this.getMaxHeadYRot(), SpellcasterIllager.this.getMaxHeadXRot()); 
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract class SpellcasterUseSpellGoal
/*     */     extends Goal
/*     */   {
/*     */     protected int attackWarmupDelay;
/*     */     protected int nextAttackTickCount;
/*     */     
/*     */     public boolean canUse() {
/* 158 */       LivingEntity target = SpellcasterIllager.this.getTarget();
/* 159 */       if (target == null || !target.isAlive()) {
/* 160 */         return false;
/*     */       }
/* 162 */       if (SpellcasterIllager.this.isCastingSpell())
/*     */       {
/* 164 */         return false;
/*     */       }
/* 166 */       if (SpellcasterIllager.this.tickCount < this.nextAttackTickCount) {
/* 167 */         return false;
/*     */       }
/* 169 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 174 */       LivingEntity target = SpellcasterIllager.this.getTarget();
/* 175 */       return (target != null && target.isAlive() && this.attackWarmupDelay > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 180 */       this.attackWarmupDelay = adjustedTickDelay(getCastWarmupTime());
/* 181 */       SpellcasterIllager.this.spellCastingTickCount = getCastingTime();
/* 182 */       this.nextAttackTickCount = SpellcasterIllager.this.tickCount + getCastingInterval();
/* 183 */       SoundEvent spellPrepareSound = getSpellPrepareSound();
/* 184 */       if (spellPrepareSound != null) {
/* 185 */         SpellcasterIllager.this.playSound(spellPrepareSound, 1.0F, 1.0F);
/*     */       }
/* 187 */       SpellcasterIllager.this.setIsCastingSpell(getSpell());
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 192 */       this.attackWarmupDelay--;
/* 193 */       if (this.attackWarmupDelay == 0) {
/* 194 */         performSpellCasting();
/* 195 */         SpellcasterIllager.this.playSound(SpellcasterIllager.this.getCastingSoundEvent(), 1.0F, 1.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract void performSpellCasting();
/*     */     
/* 202 */     protected int getCastWarmupTime() { return 20; }
/*     */     
/*     */     protected abstract int getCastingTime();
/*     */     
/*     */     protected abstract int getCastingInterval();
/*     */     
/*     */     protected abstract SoundEvent getSpellPrepareSound();
/*     */     
/*     */     protected abstract SpellcasterIllager.IllagerSpell getSpell();
/*     */   }
/*     */   
/*     */   protected enum IllagerSpell {
/*     */     private static final IntFunction<IllagerSpell> BY_ID;
/* 215 */     NONE(0, 0.0D, 0.0D, 0.0D),
/* 216 */     SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
/* 217 */     FANGS(2, 0.4D, 0.3D, 0.35D),
/* 218 */     WOLOLO(3, 0.7D, 0.5D, 0.2D),
/* 219 */     DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
/* 220 */     BLINDNESS(5, 0.1D, 0.1D, 0.2D);
/*     */     
/*     */     static  {
/* 223 */       BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     }
/*     */     
/*     */     private final int id;
/*     */     
/*     */     IllagerSpell(int id, double red, double green, double blue) {
/* 229 */       this.id = id;
/* 230 */       this.spellColor = new double[] { red, green, blue };
/*     */     }
/*     */     private final double[] spellColor;
/*     */     
/* 234 */     public static IllagerSpell byId(int id) { return (IllagerSpell)BY_ID.apply(id); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\SpellcasterIllager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */