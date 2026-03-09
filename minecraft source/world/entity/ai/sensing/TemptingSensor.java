/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class TemptingSensor extends Sensor<PathfinderMob> {
/* 23 */   private static final TargetingConditions TEMPT_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
/*    */   
/*    */   private final BiPredicate<PathfinderMob, ItemStack> temptations;
/*    */ 
/*    */   
/* 28 */   public TemptingSensor(Predicate<ItemStack> tt) { this((m, i) -> tt.test(i)); }
/*    */ 
/*    */   
/*    */   public static TemptingSensor forAnimal() {
/* 32 */     return new TemptingSensor((m, i) -> {
/* 33 */           if (m instanceof Animal) { Animal animal = (Animal)m;
/* 34 */             return animal.isFood(i); }
/*    */           
/* 36 */           return false;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   private TemptingSensor(BiPredicate<PathfinderMob, ItemStack> temptations) { this.temptations = temptations; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, PathfinderMob body) {
/* 47 */     Brain<?> brain = body.getBrain();
/* 48 */     TargetingConditions targeting = TEMPT_TARGETING.copy().range((float)body.getAttributeValue(Attributes.TEMPT_RANGE));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     Objects.requireNonNull(body);
/* 56 */     List<Player> players = (List)level.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(player -> targeting.test(level, body, player)).filter(p -> playerHoldingTemptation(body, p)).filter(player -> !body.hasPassenger(player)).sorted(Comparator.comparingDouble(body::distanceToSqr)).collect(Collectors.toList());
/*    */     
/* 58 */     if (!players.isEmpty()) {
/* 59 */       Player player = (Player)players.get(0);
/* 60 */       brain.setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
/*    */     } else {
/* 62 */       brain.eraseMemory(MemoryModuleType.TEMPTING_PLAYER);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 67 */   private boolean playerHoldingTemptation(PathfinderMob mob, Player player) { return (isTemptation(mob, player.getMainHandItem()) || isTemptation(mob, player.getOffhandItem())); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   private boolean isTemptation(PathfinderMob mob, ItemStack itemStack) { return this.temptations.test(mob, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.TEMPTING_PLAYER); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\TemptingSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */