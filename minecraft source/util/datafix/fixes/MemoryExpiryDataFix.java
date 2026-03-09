/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
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
/*    */ public class MemoryExpiryDataFix
/*    */   extends NamedEntityFix
/*    */ {
/* 30 */   public MemoryExpiryDataFix(Schema schema, String entityType) { super(schema, false, "Memory expiry data fix (" + entityType + ")", References.ENTITY, entityType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Dynamic<?> fixTag(Dynamic<?> input) { return input.update("Brain", this::updateBrain); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   private Dynamic<?> updateBrain(Dynamic<?> input) { return input.update("memories", this::updateMemories); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   private Dynamic<?> updateMemories(Dynamic<?> memories) { return memories.updateMapValues(this::updateMemoryEntry); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(Pair<Dynamic<?>, Dynamic<?>> memoryEntry) { return memoryEntry.mapSecond(this::wrapMemoryValue); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> wrapMemoryValue(Dynamic<?> dynamic) {
/* 55 */     return dynamic.createMap(ImmutableMap.of(dynamic
/* 56 */           .createString("value"), dynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\MemoryExpiryDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */