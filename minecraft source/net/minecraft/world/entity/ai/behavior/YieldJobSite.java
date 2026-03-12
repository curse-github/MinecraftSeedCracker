/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class YieldJobSite {
/*    */   public static BehaviorControl<Villager> create(float speedModifier) {
/* 23 */     return BehaviorBuilder.create(i -> i.group(i
/* 24 */           .present(MemoryModuleType.POTENTIAL_JOB_SITE), i
/* 25 */           .absent(MemoryModuleType.JOB_SITE), i
/* 26 */           .present(MemoryModuleType.NEAREST_LIVING_ENTITIES), i
/* 27 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 28 */           .registered(MemoryModuleType.LOOK_TARGET))
/* 29 */         .apply(i, ()));
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
/*    */   private static boolean nearbyWantsJobsite(Holder<PoiType> type, Villager nearbyVillager, BlockPos poiPos) {
/* 69 */     boolean nearbyHasPotentialJobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
/* 70 */     if (nearbyHasPotentialJobSite) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     Optional<GlobalPos> nearbyVillagerJobSiteMemory = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 75 */     Holder<VillagerProfession> nearbyProfession = nearbyVillager.getVillagerData().profession();
/*    */ 
/*    */     
/* 78 */     if (((VillagerProfession)nearbyProfession.value()).heldJobSite().test(type)) {
/* 79 */       if (nearbyVillagerJobSiteMemory.isEmpty()) {
/* 80 */         return canReachPos(nearbyVillager, poiPos, (PoiType)type.value());
/*    */       }
/* 82 */       return ((GlobalPos)nearbyVillagerJobSiteMemory.get()).pos().equals(poiPos);
/*    */     } 
/* 84 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean canReachPos(PathfinderMob nearbyVillager, BlockPos poiPos, PoiType type) {
/* 88 */     Path path = nearbyVillager.getNavigation().createPath(poiPos, type.validRange());
/* 89 */     return (path != null && path.canReach());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\YieldJobSite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */