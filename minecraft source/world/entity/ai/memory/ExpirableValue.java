/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpirableValue<T>
/*    */   extends Object
/*    */ {
/*    */   private final T value;
/*    */   private long timeToLive;
/*    */   
/*    */   public ExpirableValue(T value, long timeToLive) {
/* 18 */     this.value = value;
/* 19 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     if (canExpire()) {
/* 24 */       this.timeToLive--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static <T> ExpirableValue<T> of(T value) { return new ExpirableValue(value, Float.MAX_VALUE); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static <T> ExpirableValue<T> of(T value, long ticksUntilExpiry) { return new ExpirableValue(value, ticksUntilExpiry); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public long getTimeToLive() { return this.timeToLive; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public T getValue() { return (T)this.value; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean hasExpired() { return (this.timeToLive <= 0L); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String toString() { return String.valueOf(this.value) + String.valueOf(this.value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForDebug
/* 56 */   public boolean canExpire() { return (this.timeToLive != Float.MAX_VALUE); }
/*    */ 
/*    */   
/*    */   public static <T> Codec<ExpirableValue<T>> codec(Codec<T> valueCodec) {
/* 60 */     return RecordCodecBuilder.create(i -> i.group(valueCodec
/* 61 */           .fieldOf("value").forGetter(()), Codec.LONG
/* 62 */           .lenientOptionalFieldOf("ttl").forGetter(()))
/* 63 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\memory\ExpirableValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */