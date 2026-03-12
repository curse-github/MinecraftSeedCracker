/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DragonSittingFlamingPhase
/*     */   extends AbstractDragonSittingPhase
/*     */ {
/*     */   private static final int FLAME_DURATION = 200;
/*     */   private static final int SITTING_FLAME_ATTACKS_COUNT = 4;
/*     */   private static final int WARMUP_TIME = 10;
/*     */   private int flameTicks;
/*     */   private int flameCount;
/*     */   private AreaEffectCloud flame;
/*     */   
/*  26 */   public DragonSittingFlamingPhase(EnderDragon dragon) { super(dragon); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doClientTick() {
/*  31 */     this.flameTicks++;
/*     */     
/*  33 */     if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
/*  34 */       Vec3 look = this.dragon.getHeadLookVector(1.0F).normalize();
/*  35 */       look.yRot(-0.7853982F);
/*  36 */       double particleX = this.dragon.head.getX();
/*  37 */       double particleY = this.dragon.head.getY(0.5D);
/*  38 */       double particleZ = this.dragon.head.getZ();
/*  39 */       for (int i = 0; i < 8; i++) {
/*  40 */         double px = particleX + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  41 */         double py = particleY + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  42 */         double pz = particleZ + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  43 */         for (int j = 0; j < 6; j++) {
/*  44 */           this.dragon.level().addParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), px, py, pz, -look.x * 0.07999999821186066D * j, -look.y * 0.6000000238418579D, -look.z * 0.07999999821186066D * j);
/*     */         }
/*  46 */         look.yRot(0.19634955F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel level) {
/*  53 */     this.flameTicks++;
/*     */     
/*  55 */     if (this.flameTicks >= 200) {
/*  56 */       if (this.flameCount >= 4) {
/*  57 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/*     */       } else {
/*  59 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*     */       } 
/*  61 */     } else if (this.flameTicks == 10) {
/*  62 */       Vec3 look = (new Vec3(this.dragon.head.getX() - this.dragon.getX(), 0.0D, this.dragon.head.getZ() - this.dragon.getZ())).normalize();
/*  63 */       float radius = 5.0F;
/*  64 */       double x = this.dragon.head.getX() + look.x * 5.0D / 2.0D;
/*  65 */       double z = this.dragon.head.getZ() + look.z * 5.0D / 2.0D;
/*  66 */       double initialY = this.dragon.head.getY(0.5D);
/*  67 */       double y = initialY;
/*     */       
/*  69 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
/*  70 */       while (level.isEmptyBlock(pos)) {
/*  71 */         y--;
/*  72 */         if (y < 0.0D) {
/*  73 */           y = initialY;
/*     */           break;
/*     */         } 
/*  76 */         pos.set(x, y, z);
/*     */       } 
/*  78 */       y = (Mth.floor(y) + 1);
/*  79 */       this.flame = new AreaEffectCloud(level, x, y, z);
/*  80 */       this.flame.setOwner(this.dragon);
/*  81 */       this.flame.setRadius(5.0F);
/*  82 */       this.flame.setDuration(200);
/*  83 */       this.flame.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
/*  84 */       this.flame.setPotionDurationScale(0.25F);
/*  85 */       this.flame.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE));
/*  86 */       level.addFreshEntity(this.flame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  92 */     this.flameTicks = 0;
/*  93 */     this.flameCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  98 */     if (this.flame != null) {
/*  99 */       this.flame.discard();
/* 100 */       this.flame = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public EnderDragonPhase<DragonSittingFlamingPhase> getPhase() { return EnderDragonPhase.SITTING_FLAMING; }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public void resetFlameCount() { this.flameCount = 0; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingFlamingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */