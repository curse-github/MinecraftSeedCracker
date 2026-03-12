/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends MapCodec<Brain<E>>
/*     */ {
/*  80 */   public <T> Stream<T> keys(DynamicOps<T> ops) { return memoryTypes.stream()
/*  81 */       .flatMap(t -> t.getCodec().map(()).stream())
/*  82 */       .map(l -> ops.createString(l.toString())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> DataResult<Brain<E>> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  87 */     MutableObject<DataResult<ImmutableList.Builder<Brain.MemoryValue<?>>>> result = new MutableObject<DataResult<ImmutableList.Builder<Brain.MemoryValue<?>>>>(DataResult.success(ImmutableList.builder()));
/*     */     
/*  89 */     input.entries().forEach(pair -> {
/*  90 */           DataResult<MemoryModuleType<?>> typeResult = BuiltInRegistries.MEMORY_MODULE_TYPE.byNameCodec().parse(ops, pair.getFirst());
/*  91 */           DataResult<? extends Brain.MemoryValue<?>> entryResult = typeResult.flatMap(());
/*  92 */           result.setValue(((DataResult)result.get()).apply2(ImmutableList.Builder::add, entryResult));
/*     */         });
/*     */     
/*  95 */     Objects.requireNonNull(Brain.LOGGER); ImmutableList<Brain.MemoryValue<?>> memories = (ImmutableList)((DataResult)result.get()).resultOrPartial(Brain.LOGGER::error).map(ImmutableList.Builder::build).orElseGet(ImmutableList::of);
/*  96 */     return DataResult.success(new Brain(memoryTypes, sensorTypes, memories, codecReference));
/*     */   }
/*     */ 
/*     */   
/* 100 */   private <T, U> DataResult<Brain.MemoryValue<U>> captureRead(MemoryModuleType<U> type, DynamicOps<T> ops, T input) { return ((DataResult)type.getCodec().map(DataResult::success).orElseGet(() -> DataResult.error(())))
/* 101 */       .flatMap(c -> c.parse(ops, input))
/* 102 */       .map(v -> new Brain.MemoryValue(type, Optional.of(v))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(Brain<E> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 107 */     input.memories().forEach(m -> m.serialize(ops, prefix));
/* 108 */     return prefix;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\Brain$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */