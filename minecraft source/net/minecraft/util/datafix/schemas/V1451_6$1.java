/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.resources.Identifier;
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
/*     */ class null
/*     */   implements Hook.HookFunction
/*     */ {
/*     */   public <T> T apply(DynamicOps<T> ops, T value) {
/*  95 */     Dynamic<T> input = new Dynamic<T>(ops, value);
/*     */     
/*  97 */     return (T)((Dynamic)DataFixUtils.orElse(input
/*  98 */         .get("CriteriaName").asString().result()
/*  99 */         .map(name -> {
/* 100 */             int colonPos = name.indexOf(':');
/* 101 */             if (colonPos < 0) {
/* 102 */               return Pair.of("_special", name);
/*     */             }
/*     */             try {
/* 105 */               Identifier statType = Identifier.bySeparator(name.substring(0, colonPos), '.');
/* 106 */               Identifier statId = Identifier.bySeparator(name.substring(colonPos + 1), '.');
/* 107 */               return Pair.of(statType.toString(), statId.toString());
/* 108 */             } catch (Exception e) {
/* 109 */               return Pair.of("_special", name);
/*     */             }
/*     */           
/* 112 */           }).map(explodedId -> 
/* 113 */           input
/* 114 */           .set("CriteriaType", input.createMap(
/* 115 */               ImmutableMap.of(input
/* 116 */                 .createString("type"), input.createString((String)explodedId.getFirst()), input
/* 117 */                 .createString("id"), input.createString((String)explodedId.getSecond()))))), input))
/*     */ 
/*     */ 
/*     */       
/* 121 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_6$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */