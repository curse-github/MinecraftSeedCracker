/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public static enum TickPriority {
/*  6 */   EXTREMELY_HIGH(-3),
/*  7 */   VERY_HIGH(-2),
/*  8 */   HIGH(-1),
/*  9 */   NORMAL(0),
/* 10 */   LOW(1),
/* 11 */   VERY_LOW(2),
/* 12 */   EXTREMELY_LOW(3);
/*    */   
/*    */   static  {
/* 15 */     CODEC = Codec.INT.xmap(TickPriority::byValue, TickPriority::getValue);
/*    */   }
/*    */   public static final Codec<TickPriority> CODEC;
/*    */   private final int value;
/*    */   
/* 20 */   TickPriority(int value) { this.value = value; }
/*    */ 
/*    */   
/*    */   public static TickPriority byValue(int value) {
/* 24 */     for (TickPriority priority : values()) {
/* 25 */       if (priority.value == value) {
/* 26 */         return priority;
/*    */       }
/*    */     } 
/* 29 */     if (value < EXTREMELY_HIGH.value) {
/* 30 */       return EXTREMELY_HIGH;
/*    */     }
/* 32 */     return EXTREMELY_LOW;
/*    */   }
/*    */ 
/*    */   
/* 36 */   public int getValue() { return this.value; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\TickPriority.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */