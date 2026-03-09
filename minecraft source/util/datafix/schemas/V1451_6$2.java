/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
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
/* 133 */     Dynamic<T> input = new Dynamic<T>(ops, value);
/*     */     
/* 135 */     Optional<Dynamic<T>> repackedId = input.get("CriteriaType").get().result().flatMap(type -> {
/* 136 */           Optional<String> statType = type.get("type").asString().result();
/* 137 */           Optional<String> statId = type.get("id").asString().result();
/*     */           
/* 139 */           if (statType.isPresent() && statId.isPresent()) {
/* 140 */             String unpackedType = (String)statType.get();
/* 141 */             if (unpackedType.equals("_special")) {
/* 142 */               return Optional.of(input.createString((String)statId.get()));
/*     */             }
/* 144 */             return Optional.of(type.createString(V1451_6.packNamespacedWithDot(unpackedType) + ":" + V1451_6.packNamespacedWithDot(unpackedType)));
/*     */           } 
/* 146 */           return Optional.empty();
/*     */         });
/*     */     
/* 149 */     return (T)((Dynamic)DataFixUtils.orElse(repackedId.map(id -> input.set("CriteriaName", id).remove("CriteriaType")), input)).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_6$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */