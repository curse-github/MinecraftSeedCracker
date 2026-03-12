/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ 
/*    */ 
/*    */ public class PoiCompetitorScan
/*    */ {
/*    */   public static BehaviorControl<Villager> create() {
/* 22 */     return BehaviorBuilder.create(i -> i.group(i
/* 23 */           .present(MemoryModuleType.JOB_SITE), i
/* 24 */           .present(MemoryModuleType.NEAREST_LIVING_ENTITIES))
/* 25 */         .apply(i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Villager selectWinner(Villager first, Villager second) {
/*    */     Villager loser, winner;
/* 42 */     if (first.getVillagerXp() > second.getVillagerXp()) {
/* 43 */       winner = first;
/* 44 */       loser = second;
/*    */     } else {
/* 46 */       winner = second;
/* 47 */       loser = first;
/*    */     } 
/*    */     
/* 50 */     loser.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
/* 51 */     return winner;
/*    */   }
/*    */   
/*    */   private static boolean competesForSameJobsite(GlobalPos pos, Holder<PoiType> poiType, Villager nearbyVillager) {
/* 55 */     Optional<GlobalPos> jobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 56 */     return (jobSite.isPresent() && pos
/* 57 */       .equals(jobSite.get()) && 
/* 58 */       hasMatchingProfession(poiType, nearbyVillager.getVillagerData().profession()));
/*    */   }
/*    */ 
/*    */   
/* 62 */   private static boolean hasMatchingProfession(Holder<PoiType> poiType, Holder<VillagerProfession> profession) { return ((VillagerProfession)profession.value()).heldJobSite().test(poiType); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PoiCompetitorScan.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */