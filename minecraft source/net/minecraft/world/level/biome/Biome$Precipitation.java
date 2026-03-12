/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
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
/*    */ public static enum Precipitation
/*    */   implements StringRepresentable
/*    */ {
/* 70 */   NONE("none"),
/* 71 */   RAIN("rain"),
/* 72 */   SNOW("snow");
/*    */   
/*    */   static  {
/* 75 */     CODEC = StringRepresentable.fromEnum(Precipitation::values);
/*    */   }
/*    */   public static final Codec<Precipitation> CODEC;
/*    */   private final String name;
/*    */   
/* 80 */   Precipitation(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 85 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Biome$Precipitation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */