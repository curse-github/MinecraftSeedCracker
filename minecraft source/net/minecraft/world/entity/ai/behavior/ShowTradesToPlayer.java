/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShowTradesToPlayer
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private static final int MAX_LOOK_TIME = 900;
/*     */   private static final int STARTING_LOOK_TIME = 40;
/*     */   private ItemStack playerItemStack;
/*  27 */   private final List<ItemStack> displayItems = Lists.newArrayList();
/*     */   private int cycleCounter;
/*     */   private int displayIndex;
/*     */   private int lookTime;
/*     */   
/*     */   public ShowTradesToPlayer(int minDuration, int maxDuration) {
/*  33 */     super(
/*  34 */         ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT), minDuration, maxDuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  44 */     Brain<?> brain = body.getBrain();
/*  45 */     if (brain.getMemory(MemoryModuleType.INTERACTION_TARGET).isEmpty()) {
/*  46 */       return false;
/*     */     }
/*     */     
/*  49 */     LivingEntity target = (LivingEntity)brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  50 */     return (target.getType() == EntityType.PLAYER && body
/*  51 */       .isAlive() && target
/*  52 */       .isAlive() && 
/*  53 */       !body.isBaby() && body
/*  54 */       .distanceToSqr(target) <= 17.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  59 */     return (checkExtraStartConditions(level, body) && this.lookTime > 0 && body
/*     */       
/*  61 */       .getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(ServerLevel level, Villager body, long timestamp) {
/*  66 */     super.start(level, body, timestamp);
/*  67 */     lookAtTarget(body);
/*     */     
/*  69 */     this.cycleCounter = 0;
/*  70 */     this.displayIndex = 0;
/*  71 */     this.lookTime = 40;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(ServerLevel level, Villager body, long timestamp) {
/*  76 */     LivingEntity target = lookAtTarget(body);
/*     */     
/*  78 */     findItemsToDisplay(target, body);
/*  79 */     if (!this.displayItems.isEmpty()) {
/*  80 */       displayCyclingItems(body);
/*     */     } else {
/*  82 */       clearHeldItem(body);
/*  83 */       this.lookTime = Math.min(this.lookTime, 40);
/*     */     } 
/*     */     
/*  86 */     this.lookTime--;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(ServerLevel level, Villager body, long timestamp) {
/*  91 */     super.stop(level, body, timestamp);
/*  92 */     body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*     */     
/*  94 */     clearHeldItem(body);
/*  95 */     this.playerItemStack = null;
/*     */   }
/*     */   
/*     */   private void findItemsToDisplay(LivingEntity player, Villager villager) {
/*  99 */     boolean changed = false;
/* 100 */     ItemStack currentPlayerItemStack = player.getMainHandItem();
/* 101 */     if (this.playerItemStack == null || !ItemStack.isSameItem(this.playerItemStack, currentPlayerItemStack)) {
/* 102 */       this.playerItemStack = currentPlayerItemStack;
/* 103 */       changed = true;
/* 104 */       this.displayItems.clear();
/*     */     } 
/*     */     
/* 107 */     if (changed && !this.playerItemStack.isEmpty()) {
/* 108 */       updateDisplayItems(villager);
/* 109 */       if (!this.displayItems.isEmpty()) {
/* 110 */         this.lookTime = 900;
/* 111 */         displayFirstItem(villager);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 117 */   private void displayFirstItem(Villager villager) { displayAsHeldItem(villager, (ItemStack)this.displayItems.get(0)); }
/*     */ 
/*     */   
/*     */   private void updateDisplayItems(Villager villager) {
/* 121 */     for (MerchantOffer offer : villager.getOffers()) {
/* 122 */       if (!offer.isOutOfStock() && playerItemStackMatchesCostOfOffer(offer)) {
/* 123 */         this.displayItems.add(offer.assemble());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 129 */   private boolean playerItemStackMatchesCostOfOffer(MerchantOffer offer) { return (ItemStack.isSameItem(this.playerItemStack, offer.getCostA()) || ItemStack.isSameItem(this.playerItemStack, offer.getCostB())); }
/*     */ 
/*     */   
/*     */   private static void clearHeldItem(Villager body) {
/* 133 */     body.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 134 */     body.setDropChance(EquipmentSlot.MAINHAND, 0.085F);
/*     */   }
/*     */   
/*     */   private static void displayAsHeldItem(Villager body, ItemStack itemStack) {
/* 138 */     body.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/* 139 */     body.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
/*     */   }
/*     */   
/*     */   private LivingEntity lookAtTarget(Villager myBody) {
/* 143 */     Brain<?> brain = myBody.getBrain();
/*     */     
/* 145 */     LivingEntity target = (LivingEntity)brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */     
/* 147 */     brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
/* 148 */     return target;
/*     */   }
/*     */   
/*     */   private void displayCyclingItems(Villager villager) {
/* 152 */     if (this.displayItems.size() >= 2 && ++this.cycleCounter >= 40) {
/* 153 */       this.displayIndex++;
/* 154 */       this.cycleCounter = 0;
/* 155 */       if (this.displayIndex > this.displayItems.size() - 1) {
/* 156 */         this.displayIndex = 0;
/*     */       }
/* 158 */       displayAsHeldItem(villager, (ItemStack)this.displayItems.get(this.displayIndex));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ShowTradesToPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */