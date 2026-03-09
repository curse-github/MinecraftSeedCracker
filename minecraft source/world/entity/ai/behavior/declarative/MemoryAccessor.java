/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MemoryAccessor<F extends K1, Value>
/*    */   extends Object
/*    */ {
/*    */   private final Brain<?> brain;
/*    */   private final MemoryModuleType<Value> memoryType;
/*    */   private final App<F, Value> value;
/*    */   
/*    */   public MemoryAccessor(Brain<?> brain, MemoryModuleType<Value> memoryType, App<F, Value> value) {
/* 20 */     this.brain = brain;
/* 21 */     this.memoryType = memoryType;
/* 22 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/* 26 */   public App<F, Value> value() { return this.value; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void set(Value value) { this.brain.setMemory(this.memoryType, Optional.of(value)); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void setOrErase(Optional<Value> value) { this.brain.setMemory(this.memoryType, value); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void setWithExpiry(Value value, long timeToLive) { this.brain.setMemoryWithExpiry(this.memoryType, value, timeToLive); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void erase() { this.brain.eraseMemory(this.memoryType); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\MemoryAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */