/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TradeWithVillager
/*     */   extends Behavior<Villager>
/*     */ {
/*  24 */   private Set<Item> trades = ImmutableSet.of();
/*     */ 
/*     */   
/*  27 */   public TradeWithVillager() { super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) { return BehaviorUtils.targetIsValid(body.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return checkExtraStartConditions(level, body); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager myBody, long timestamp) {
/*  45 */     Villager target = (Villager)myBody.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  46 */     BehaviorUtils.lockGazeAndWalkToEachOther(myBody, target, 0.5F, 2);
/*     */     
/*  48 */     this.trades = figureOutWhatIAmWillingToTrade(myBody, target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  53 */     Villager target = (Villager)body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */     
/*  55 */     if (body.distanceToSqr(target) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     BehaviorUtils.lockGazeAndWalkToEachOther(body, target, 0.5F, 2);
/*     */     
/*  61 */     body.gossip(level, target, timestamp);
/*     */     
/*  63 */     boolean isFarmer = body.getVillagerData().profession().is(VillagerProfession.FARMER);
/*  64 */     if (body.hasExcessFood() && (isFarmer || target.wantsMoreFood())) {
/*  65 */       throwHalfStack(body, Villager.FOOD_POINTS.keySet(), target);
/*     */     }
/*     */     
/*  68 */     if (isFarmer && body.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getDefaultMaxStackSize() / 2) {
/*  69 */       throwHalfStack(body, ImmutableSet.of(Items.WHEAT), target);
/*     */     }
/*     */     
/*  72 */     if (!this.trades.isEmpty() && body.getInventory().hasAnyOf(this.trades)) {
/*  73 */       throwHalfStack(body, this.trades, target);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   protected void stop(ServerLevel level, Villager body, long timestamp) { body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Item> figureOutWhatIAmWillingToTrade(Villager myBody, Villager target) {
/*  85 */     ImmutableSet<Item> targetItems = ((VillagerProfession)target.getVillagerData().profession().value()).requestedItems();
/*  86 */     ImmutableSet<Item> selfItems = ((VillagerProfession)myBody.getVillagerData().profession().value()).requestedItems();
/*  87 */     return (Set)targetItems.stream().filter(entry -> !selfItems.contains(entry)).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwHalfStack(Villager villager, Set<Item> items, LivingEntity target) {
/*  94 */     SimpleContainer inventory = villager.getInventory();
/*     */     
/*  96 */     ItemStack toThrow = ItemStack.EMPTY;
/*  97 */     for (int i = 0; i < inventory.getContainerSize(); i++) {
/*  98 */       ItemStack itemStack = inventory.getItem(i);
/*  99 */       if (!itemStack.isEmpty()) {
/* 100 */         Item item = itemStack.getItem();
/* 101 */         if (items.contains(item)) {
/*     */           int count;
/* 103 */           if (itemStack.getCount() > itemStack.getMaxStackSize() / 2) {
/* 104 */             count = itemStack.getCount() / 2;
/* 105 */           } else if (itemStack.getCount() > 24) {
/* 106 */             count = itemStack.getCount() - 24;
/*     */           } else {
/*     */             continue;
/*     */           } 
/* 110 */           itemStack.shrink(count);
/* 111 */           toThrow = new ItemStack(item, count);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       continue;
/*     */     } 
/* 117 */     if (!toThrow.isEmpty())
/* 118 */       BehaviorUtils.throwItem(villager, toThrow, target.position()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TradeWithVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */