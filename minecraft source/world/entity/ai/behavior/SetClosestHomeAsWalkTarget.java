/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class SetClosestHomeAsWalkTarget {
/*    */   private static final int CACHE_TIMEOUT = 40;
/*    */   private static final int BATCH_SIZE = 5;
/*    */   private static final int RATE = 20;
/*    */   private static final int OK_DISTANCE_SQR = 4;
/*    */   
/*    */   public static BehaviorControl<PathfinderMob> create(float speedModifier) {
/* 34 */     Long2LongOpenHashMap long2LongOpenHashMap = new Long2LongOpenHashMap();
/* 35 */     MutableLong lastUpdate = new MutableLong(0L);
/*    */     
/* 37 */     return BehaviorBuilder.create(i -> i.group(i
/* 38 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 39 */           .absent(MemoryModuleType.HOME))
/* 40 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetClosestHomeAsWalkTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */