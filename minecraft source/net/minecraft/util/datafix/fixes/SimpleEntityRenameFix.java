/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public abstract class SimpleEntityRenameFix
/*    */   extends EntityRenameFix {
/* 11 */   public SimpleEntityRenameFix(String name, Schema outputSchema, boolean changesType) { super(name, outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 16 */     Pair<String, Dynamic<?>> pair = getNewNameAndTag(name, (Dynamic)entity.getOrCreate(DSL.remainderFinder()));
/* 17 */     return Pair.of((String)pair.getFirst(), entity.set(DSL.remainderFinder(), (Dynamic)pair.getSecond()));
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\SimpleEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */