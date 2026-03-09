/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LlamaFollowCaravanGoal
/*     */   extends Goal
/*     */ {
/*     */   public final Llama llama;
/*     */   private double speedModifier;
/*     */   private static final int CARAVAN_LIMIT = 8;
/*     */   private int distCheckCounter;
/*     */   
/*     */   public LlamaFollowCaravanGoal(Llama llama, double speedModifier) {
/*  23 */     this.llama = llama;
/*  24 */     this.speedModifier = speedModifier;
/*  25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  30 */     if (this.llama.isLeashed() || this.llama.inCaravan()) {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     List<Entity> llamas = this.llama.level().getEntities(this.llama, this.llama.getBoundingBox().inflate(9.0D, 4.0D, 9.0D), e -> {
/*  35 */           EntityType<?> type = e.getType();
/*  36 */           return (type == EntityType.LLAMA || type == EntityType.TRADER_LLAMA);
/*     */         });
/*     */     
/*  39 */     Llama closest = null;
/*  40 */     double closestDistSquare = Double.MAX_VALUE;
/*  41 */     for (Entity entity : llamas) {
/*  42 */       Llama candidate = (Llama)entity;
/*     */       
/*  44 */       if (!candidate.inCaravan() || candidate.hasCaravanTail()) {
/*     */         continue;
/*     */       }
/*     */       
/*  48 */       double distSquare = this.llama.distanceToSqr(candidate);
/*  49 */       if (distSquare > closestDistSquare) {
/*     */         continue;
/*     */       }
/*     */       
/*  53 */       closestDistSquare = distSquare;
/*  54 */       closest = candidate;
/*     */     } 
/*     */     
/*  57 */     if (closest == null)
/*     */     {
/*  59 */       for (Entity entity : llamas) {
/*  60 */         Llama candidate = (Llama)entity;
/*     */         
/*  62 */         if (!candidate.isLeashed()) {
/*     */           continue;
/*     */         }
/*     */         
/*  66 */         if (candidate.hasCaravanTail()) {
/*     */           continue;
/*     */         }
/*     */         
/*  70 */         double distSquare = this.llama.distanceToSqr(candidate);
/*  71 */         if (distSquare > closestDistSquare) {
/*     */           continue;
/*     */         }
/*     */         
/*  75 */         closestDistSquare = distSquare;
/*  76 */         closest = candidate;
/*     */       } 
/*     */     }
/*     */     
/*  80 */     if (closest == null) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (closestDistSquare < 4.0D) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     if (!closest.isLeashed() && !firstIsLeashed(closest, 1)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     this.llama.joinCaravan(closest);
/*     */     
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  98 */     if (!this.llama.inCaravan() || !this.llama.getCaravanHead().isAlive() || !firstIsLeashed(this.llama, 0)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     double distSqr = this.llama.distanceToSqr(this.llama.getCaravanHead());
/* 103 */     if (distSqr > 676.0D) {
/* 104 */       if (this.speedModifier <= 3.0D) {
/* 105 */         this.speedModifier *= 1.2D;
/* 106 */         this.distCheckCounter = reducedTickDelay(40);
/* 107 */         return true;
/*     */       } 
/*     */       
/* 110 */       if (this.distCheckCounter == 0) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/* 114 */     if (this.distCheckCounter > 0) {
/* 115 */       this.distCheckCounter--;
/*     */     }
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 122 */     this.llama.leaveCaravan();
/* 123 */     this.speedModifier = 2.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 128 */     if (!this.llama.inCaravan()) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (this.llama.getLeashHolder() instanceof net.minecraft.world.entity.decoration.LeashFenceKnotEntity) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     Llama follows = this.llama.getCaravanHead();
/* 137 */     double distanceTo = this.llama.distanceTo(follows);
/*     */     
/* 139 */     float wantedDistance = 2.0F;
/* 140 */     Vec3 delta = (new Vec3(follows.getX() - this.llama.getX(), follows.getY() - this.llama.getY(), follows.getZ() - this.llama.getZ())).normalize().scale(Math.max(distanceTo - 2.0D, 0.0D));
/* 141 */     this.llama.getNavigation().moveTo(this.llama.getX() + delta.x, this.llama.getY() + delta.y, this.llama.getZ() + delta.z, this.speedModifier);
/*     */   }
/*     */   
/*     */   private boolean firstIsLeashed(Llama currentMob, int counter) {
/* 145 */     if (counter > 8) {
/* 146 */       return false;
/*     */     }
/*     */     
/* 149 */     if (currentMob.inCaravan()) {
/* 150 */       if (currentMob.getCaravanHead().isLeashed()) {
/* 151 */         return true;
/*     */       }
/* 153 */       return firstIsLeashed(currentMob.getCaravanHead(), ++counter);
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\LlamaFollowCaravanGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */