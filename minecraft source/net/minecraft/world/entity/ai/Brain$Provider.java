/*    */ package net.minecraft.world.entity.ai;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ import net.minecraft.world.entity.ai.sensing.SensorType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Provider<E extends LivingEntity>
/*    */   extends Object
/*    */ {
/*    */   private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*    */   private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
/*    */   private final Codec<Brain<E>> codec;
/*    */   
/*    */   private Provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) {
/* 60 */     this.memoryTypes = memoryTypes;
/* 61 */     this.sensorTypes = sensorTypes;
/* 62 */     this.codec = Brain.codec(memoryTypes, sensorTypes);
/*    */   }
/*    */ 
/*    */   
/* 66 */   public Brain<E> makeBrain(Dynamic<?> input) { Objects.requireNonNull(Brain.LOGGER); return (Brain)this.codec.parse(input).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain(this.memoryTypes, this.sensorTypes, ImmutableList.of(), ())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\Brain$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */