/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MemoryValue<U>
/*     */   extends Object
/*     */ {
/*     */   private final MemoryModuleType<U> type;
/*     */   private final Optional<? extends ExpirableValue<U>> value;
/*     */   
/* 174 */   private static <U> MemoryValue<U> createUnchecked(MemoryModuleType<U> type, Optional<? extends ExpirableValue<?>> value) { return new MemoryValue(type, value); }
/*     */ 
/*     */   
/*     */   private MemoryValue(MemoryModuleType<U> type, Optional<? extends ExpirableValue<U>> value) {
/* 178 */     this.type = type;
/* 179 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/* 183 */   private void setMemoryInternal(Brain<?> brain) { brain.setMemoryInternal(this.type, this.value); }
/*     */ 
/*     */   
/*     */   public <T> void serialize(DynamicOps<T> ops, RecordBuilder<T> builder) {
/* 187 */     this.type.getCodec().ifPresent(codec -> this.value.ifPresent(()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\Brain$MemoryValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */