/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ public class ValidateNearbyPoi
/*    */ {
/*    */   private static final int MAX_DISTANCE = 16;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryType) {
/* 28 */     return BehaviorBuilder.create(i -> i.group(i
/* 29 */           .present(memoryType))
/* 30 */         .apply(i, ()));
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
/*    */   private static boolean bedIsOccupied(ServerLevel poiLevel, BlockPos poiPos, LivingEntity body) {
/* 54 */     BlockState blockState = poiLevel.getBlockState(poiPos);
/* 55 */     return (blockState.is(BlockTags.BEDS) && ((Boolean)blockState.getValue(BedBlock.OCCUPIED)).booleanValue() && !body.isSleeping());
/*    */   }
/*    */   
/*    */   private static boolean bedIsOccupiedByVillager(ServerLevel poiLevel, BlockPos poiPos) {
/* 59 */     List<Villager> villagers = poiLevel.getEntitiesOfClass(Villager.class, new AABB(poiPos), LivingEntity::isSleeping);
/* 60 */     return !villagers.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ValidateNearbyPoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */