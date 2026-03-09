/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.RegistryOps;
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
/*    */   implements Codec<TypedEntityData<T>>
/*    */ {
/*    */   public <V> DataResult<Pair<TypedEntityData<T>, V>> decode(DynamicOps<V> ops, V input) {
/* 42 */     return CustomData.COMPOUND_TAG_CODEC.decode(ops, input).flatMap(pair -> {
/* 43 */           CompoundTag tagWithoutType = ((CompoundTag)pair.getFirst()).copy();
/* 44 */           Tag typeTag = tagWithoutType.remove("id");
/* 45 */           if (typeTag == null) {
/* 46 */             return DataResult.error(());
/*    */           }
/* 48 */           return typeCodec.parse(asNbtOps(ops), typeTag).map(());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> DataResult<V> encode(TypedEntityData<T> input, DynamicOps<V> ops, V prefix) {
/* 54 */     return typeCodec.encodeStart(asNbtOps(ops), input.type).flatMap(typeTag -> {
/* 55 */           CompoundTag tag = input.tag.copy();
/* 56 */           tag.put("id", typeTag);
/* 57 */           return CustomData.COMPOUND_TAG_CODEC.encode(tag, ops, prefix);
/*    */         });
/*    */   }
/*    */   
/*    */   private static <T> DynamicOps<Tag> asNbtOps(DynamicOps<T> ops) {
/* 62 */     if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps)ops;
/* 63 */       return registryOps.withParent(NbtOps.INSTANCE); }
/*    */     
/* 65 */     return NbtOps.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\TypedEntityData$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */