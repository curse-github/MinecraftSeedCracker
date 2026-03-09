/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.GsonHelper;
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
/*    */ class null
/*    */   extends Object
/*    */   implements Codec<Component>
/*    */ {
/*    */   public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
/* 53 */     return ComponentSerialization.CODEC.decode(ops, input).flatMap(pair -> {
/* 54 */           if (isTooLarge(ops, (Component)pair.getFirst())) {
/* 55 */             return DataResult.error(());
/*    */           }
/* 57 */           return DataResult.success(pair);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) { return ComponentSerialization.CODEC.encodeStart(ops, input); }
/*    */ 
/*    */   
/*    */   private <T> boolean isTooLarge(DynamicOps<T> ops, Component input) {
/* 67 */     DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(asJsonOps(ops), input);
/* 68 */     return (json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), maxFlatSize));
/*    */   }
/*    */   
/*    */   private static <T> DynamicOps<JsonElement> asJsonOps(DynamicOps<T> ops) {
/* 72 */     if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps)ops;
/* 73 */       return registryOps.withParent(JsonOps.INSTANCE); }
/*    */     
/* 75 */     return JsonOps.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentSerialization$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */