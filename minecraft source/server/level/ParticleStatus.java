/*    */ package net.minecraft.server.level;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum ParticleStatus {
/*    */   private static final IntFunction<ParticleStatus> BY_ID;
/* 10 */   ALL(0, "options.particles.all"),
/* 11 */   DECREASED(1, "options.particles.decreased"),
/* 12 */   MINIMAL(2, "options.particles.minimal"); public static final Codec<ParticleStatus> LEGACY_CODEC;
/*    */   
/*    */   static  {
/* 15 */     BY_ID = ByIdMap.continuous(s -> s.id, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 16 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, s -> Integer.valueOf(s.id));
/*    */   }
/*    */   private final int id;
/*    */   private final Component caption;
/*    */   
/*    */   ParticleStatus(int id, String key) {
/* 22 */     this.id = id;
/* 23 */     this.caption = Component.translatable(key);
/*    */   }
/*    */ 
/*    */   
/* 27 */   public Component caption() { return this.caption; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ParticleStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */