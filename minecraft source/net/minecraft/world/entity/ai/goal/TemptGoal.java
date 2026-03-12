/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TemptGoal
/*     */   extends Goal {
/*  17 */   private static final TargetingConditions TEMPT_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
/*     */   
/*     */   private static final double DEFAULT_STOP_DISTANCE = 2.5D;
/*     */   
/*     */   private final TargetingConditions targetingConditions;
/*     */   protected final Mob mob;
/*     */   protected final double speedModifier;
/*     */   private double px;
/*     */   private double py;
/*     */   private double pz;
/*     */   private double pRotX;
/*     */   private double pRotY;
/*     */   protected Player player;
/*     */   private int calmDown;
/*     */   private boolean isRunning;
/*     */   private final Predicate<ItemStack> items;
/*     */   private final boolean canScare;
/*     */   private final double stopDistance;
/*     */   
/*  36 */   public TemptGoal(PathfinderMob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare) { this(mob, speedModifier, items, canScare, 2.5D); }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public TemptGoal(PathfinderMob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) { this(mob, speedModifier, items, canScare, stopDistance); }
/*     */ 
/*     */   
/*     */   private TemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
/*  44 */     this.mob = mob;
/*  45 */     this.speedModifier = speedModifier;
/*  46 */     this.items = items;
/*  47 */     this.canScare = canScare;
/*  48 */     this.stopDistance = stopDistance;
/*  49 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  50 */     this.targetingConditions = TEMPT_TARGETING.copy().selector((target, level) -> shouldFollow(target));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  55 */     if (this.calmDown > 0) {
/*  56 */       this.calmDown--;
/*  57 */       return false;
/*     */     } 
/*  59 */     this.player = getServerLevel(this.mob).getNearestPlayer(this.targetingConditions.range(this.mob.getAttributeValue(Attributes.TEMPT_RANGE)), this.mob);
/*  60 */     return (this.player != null);
/*     */   }
/*     */ 
/*     */   
/*  64 */   private boolean shouldFollow(LivingEntity player) { return (this.items.test(player.getMainHandItem()) || this.items.test(player.getOffhandItem())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  69 */     if (canScare()) {
/*  70 */       if (this.mob.distanceToSqr(this.player) < 36.0D) {
/*  71 */         if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D) {
/*  72 */           return false;
/*     */         }
/*  74 */         if (Math.abs(this.player.getXRot() - this.pRotX) > 5.0D || Math.abs(this.player.getYRot() - this.pRotY) > 5.0D) {
/*  75 */           return false;
/*     */         }
/*     */       } else {
/*  78 */         this.px = this.player.getX();
/*  79 */         this.py = this.player.getY();
/*  80 */         this.pz = this.player.getZ();
/*     */       } 
/*  82 */       this.pRotX = this.player.getXRot();
/*  83 */       this.pRotY = this.player.getYRot();
/*     */     } 
/*  85 */     return canUse();
/*     */   }
/*     */ 
/*     */   
/*  89 */   protected boolean canScare() { return this.canScare; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  94 */     this.px = this.player.getX();
/*  95 */     this.py = this.player.getY();
/*  96 */     this.pz = this.player.getZ();
/*  97 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 102 */     this.player = null;
/* 103 */     stopNavigation();
/* 104 */     this.calmDown = reducedTickDelay(100);
/* 105 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 110 */     this.mob.getLookControl().setLookAt(this.player, (this.mob.getMaxHeadYRot() + 20), this.mob.getMaxHeadXRot());
/* 111 */     if (this.mob.distanceToSqr(this.player) < this.stopDistance * this.stopDistance) {
/* 112 */       stopNavigation();
/*     */     } else {
/* 114 */       navigateTowards(this.player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 119 */   protected void stopNavigation() { this.mob.getNavigation().stop(); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected void navigateTowards(Player player) { this.mob.getNavigation().moveTo(player, this.speedModifier); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean isRunning() { return this.isRunning; }
/*     */   
/*     */   public static class ForNonPathfinders
/*     */     extends TemptGoal
/*     */   {
/* 132 */     public ForNonPathfinders(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) { super(mob, speedModifier, items, canScare, stopDistance); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     protected void stopNavigation() { this.mob.getMoveControl().setWait(); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void navigateTowards(Player player) {
/* 142 */       Vec3 target = player.getEyePosition().subtract(this.mob.position()).scale(this.mob.getRandom().nextDouble()).add(this.mob.position());
/* 143 */       this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\TemptGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */