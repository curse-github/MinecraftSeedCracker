/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Type
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final Codec<Type> CODEC;
/* 54 */   CLEAR("clear", 100000, 0, false, false),
/* 55 */   RAIN("rain", 0, 100000, true, false),
/* 56 */   THUNDER("thunder", 0, 100000, true, true);
/*    */   
/*    */   static  {
/* 59 */     CODEC = StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */ 
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final int clearTime;
/*    */   
/*    */   Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/* 68 */     this.id = id;
/* 69 */     this.clearTime = clearTime;
/* 70 */     this.rainTime = rainTime;
/* 71 */     this.raining = raining;
/* 72 */     this.thundering = thundering;
/*    */   }
/*    */   private final int rainTime; private final boolean raining; private final boolean thundering;
/*    */   
/* 76 */   void apply(ServerLevel level) { level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Weather$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */