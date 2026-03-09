/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DummySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {}
/*    */   
/* 17 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\DummySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */