/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.CriterionTrigger;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public abstract class SimpleCriterionTrigger<T extends SimpleCriterionTrigger.SimpleInstance>
/*    */   extends Object implements CriterionTrigger<T> {
/* 19 */   private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> players = Maps.newIdentityHashMap();
/*    */ 
/*    */ 
/*    */   
/* 23 */   public final void addPlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<T> listener) { ((Set)this.players.computeIfAbsent(player, k -> Sets.newHashSet())).add(listener); }
/*    */ 
/*    */ 
/*    */   
/*    */   public final void removePlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<T> listener) {
/* 28 */     Set<CriterionTrigger.Listener<T>> listeners = (Set)this.players.get(player);
/* 29 */     if (listeners != null) {
/* 30 */       listeners.remove(listener);
/* 31 */       if (listeners.isEmpty()) {
/* 32 */         this.players.remove(player);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public final void removePlayerListeners(PlayerAdvancements player) { this.players.remove(player); }
/*    */ 
/*    */   
/*    */   protected void trigger(ServerPlayer player, Predicate<T> matcher) {
/* 43 */     PlayerAdvancements advancements = player.getAdvancements();
/* 44 */     Set<CriterionTrigger.Listener<T>> allListeners = (Set)this.players.get(advancements);
/*    */     
/* 46 */     if (allListeners == null || allListeners.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     LootContext playerContext = EntityPredicate.createContext(player, player);
/*    */     
/* 52 */     List<CriterionTrigger.Listener<T>> listeners = null;
/* 53 */     for (CriterionTrigger.Listener<T> listener : allListeners) {
/* 54 */       T triggerInstance = (T)(SimpleInstance)listener.trigger();
/*    */ 
/*    */       
/* 57 */       if (!matcher.test(triggerInstance)) {
/*    */         continue;
/*    */       }
/* 60 */       Optional<ContextAwarePredicate> predicate = triggerInstance.player();
/* 61 */       if (predicate.isEmpty() || ((ContextAwarePredicate)predicate.get()).matches(playerContext)) {
/* 62 */         if (listeners == null) {
/* 63 */           listeners = Lists.newArrayList();
/*    */         }
/* 65 */         listeners.add(listener);
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     if (listeners != null) {
/* 70 */       for (CriterionTrigger.Listener<T> listener : listeners) {
/* 71 */         listener.run(advancements);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface SimpleInstance
/*    */     extends CriterionTriggerInstance
/*    */   {
/* 79 */     default void validate(CriterionValidator validator) { validator.validateEntity(player(), "player"); }
/*    */     
/*    */     Optional<ContextAwarePredicate> player();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SimpleCriterionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */