/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObtainRaidLeaderBannerGoal<T extends Raider>
/*     */   extends Goal
/*     */ {
/*     */   private final T mob;
/*     */   private Int2LongOpenHashMap unreachableBannerCache;
/*     */   private Path pathToBanner;
/*     */   private ItemEntity pursuedBannerItemEntity;
/*     */   
/*     */   public ObtainRaidLeaderBannerGoal(T mob) {
/* 289 */     this.unreachableBannerCache = new Int2LongOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     this.mob = mob;
/* 295 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 300 */     if (cannotPickUpBanner()) {
/* 301 */       return false;
/*     */     }
/*     */     
/* 304 */     Int2LongOpenHashMap tempCache = new Int2LongOpenHashMap();
/* 305 */     double followRange = Raider.this.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 306 */     List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(followRange, 8.0D, followRange), Raider.ALLOWED_ITEMS);
/*     */     
/* 308 */     for (ItemEntity banner : items) {
/* 309 */       long unreachableUntilTime = this.unreachableBannerCache.getOrDefault(banner.getId(), Float.MIN_VALUE);
/* 310 */       if (Raider.this.level().getGameTime() < unreachableUntilTime) {
/* 311 */         tempCache.put(banner.getId(), unreachableUntilTime);
/*     */         
/*     */         continue;
/*     */       } 
/* 315 */       Path path = this.mob.getNavigation().createPath(banner, 1);
/* 316 */       if (path != null && path.canReach()) {
/* 317 */         this.pathToBanner = path;
/* 318 */         this.pursuedBannerItemEntity = banner;
/* 319 */         return true;
/*     */       } 
/* 321 */       tempCache.put(banner.getId(), Raider.this.level().getGameTime() + 600L);
/*     */     } 
/*     */ 
/*     */     
/* 325 */     this.unreachableBannerCache = tempCache;
/*     */     
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 332 */     if (this.pursuedBannerItemEntity == null || this.pathToBanner == null) {
/* 333 */       return false;
/*     */     }
/* 335 */     if (this.pursuedBannerItemEntity.isRemoved()) {
/* 336 */       return false;
/*     */     }
/* 338 */     if (this.pathToBanner.isDone()) {
/* 339 */       return false;
/*     */     }
/* 341 */     if (cannotPickUpBanner()) {
/* 342 */       return false;
/*     */     }
/* 344 */     return true;
/*     */   }
/*     */   
/*     */   private boolean cannotPickUpBanner() {
/* 348 */     if (!this.mob.hasActiveRaid()) {
/* 349 */       return true;
/*     */     }
/* 351 */     if (this.mob.getCurrentRaid().isOver()) {
/* 352 */       return true;
/*     */     }
/* 354 */     if (!this.mob.canBeLeader()) {
/* 355 */       return true;
/*     */     }
/* 357 */     if (ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getOminousBannerInstance(this.mob.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))) {
/* 358 */       return true;
/*     */     }
/* 360 */     Raider leader = Raider.this.raid.getLeader(this.mob.getWave());
/* 361 */     if (leader != null && leader.isAlive()) {
/* 362 */       return true;
/*     */     }
/* 364 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 369 */   public void start() { this.mob.getNavigation().moveTo(this.pathToBanner, 1.149999976158142D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 374 */     this.pathToBanner = null;
/* 375 */     this.pursuedBannerItemEntity = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 380 */     if (this.pursuedBannerItemEntity != null && this.pursuedBannerItemEntity.closerThan(this.mob, 1.414D))
/* 381 */       this.mob.pickUpItem(getServerLevel(Raider.this.level()), this.pursuedBannerItemEntity); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raider$ObtainRaidLeaderBannerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */