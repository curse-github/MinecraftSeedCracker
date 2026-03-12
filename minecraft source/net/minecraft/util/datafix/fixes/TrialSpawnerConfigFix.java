/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class TrialSpawnerConfigFix
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/* 14 */   public TrialSpawnerConfigFix(Schema outputSchema) { super(outputSchema, true, "Trial Spawner config tag fixer", References.BLOCK_ENTITY, "minecraft:trial_spawner"); }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> moveToConfigTag(Dynamic<T> input) {
/* 18 */     List<String> keysToMove = List.of("spawn_range", "total_mobs", "simultaneous_mobs", "total_mobs_added_per_player", "simultaneous_mobs_added_per_player", "ticks_between_spawn", "spawn_potentials", "loot_tables_to_eject", "items_to_drop_when_ominous");
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
/* 30 */     Map<Dynamic<T>, Dynamic<T>> map = new HashMap<Dynamic<T>, Dynamic<T>>(keysToMove.size());
/* 31 */     for (String key : keysToMove) {
/* 32 */       Optional<Dynamic<T>> maybeValueForKey = input.get(key).get().result();
/* 33 */       if (maybeValueForKey.isPresent()) {
/* 34 */         map.put(input.createString(key), (Dynamic)maybeValueForKey.get());
/* 35 */         input = input.remove(key);
/*    */       } 
/*    */     } 
/* 38 */     return map.isEmpty() ? input : input.set("normal_config", input.createMap(map));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected <T> Dynamic<T> fix(Dynamic<T> input) { return moveToConfigTag(input); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TrialSpawnerConfigFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */