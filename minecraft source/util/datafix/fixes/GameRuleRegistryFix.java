/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GameRuleRegistryFix extends DataFix {
/*  12 */   public GameRuleRegistryFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  17 */     return fixTypeEverywhereTyped("GameRuleRegistryFix", getInputSchema().getType(References.LEVEL), input -> 
/*  18 */         input.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue) { return convertInteger(oldValue, -2147483648, 2147483647); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min) { return convertInteger(oldValue, min, 2147483647); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min, int max) {
/* 113 */     String stringValue = oldValue.asString("");
/*     */     try {
/* 115 */       int parsedValue = Integer.parseInt(stringValue);
/* 116 */       return oldValue.createInt(Mth.clamp(parsedValue, min, max));
/* 117 */     } catch (NumberFormatException ignored) {
/* 118 */       return oldValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 123 */   private static Dynamic<?> convertBoolean(Dynamic<?> oldValue) { return oldValue.createBoolean(Boolean.parseBoolean(oldValue.asString(""))); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static Dynamic<?> convertBooleanInverted(Dynamic<?> oldValue) { return oldValue.createBoolean(!Boolean.parseBoolean(oldValue.asString(""))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\GameRuleRegistryFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */