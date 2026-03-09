/*    */ package net.minecraft.world.level;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum MoonPhase implements StringRepresentable {
/*    */   public static final Codec<MoonPhase> CODEC;
/*    */   public static final int COUNT;
/*  8 */   FULL_MOON(0, "full_moon"),
/*  9 */   WANING_GIBBOUS(1, "waning_gibbous"),
/* 10 */   THIRD_QUARTER(2, "third_quarter"),
/* 11 */   WANING_CRESCENT(3, "waning_crescent"),
/* 12 */   NEW_MOON(4, "new_moon"),
/* 13 */   WAXING_CRESCENT(5, "waxing_crescent"),
/* 14 */   FIRST_QUARTER(6, "first_quarter"),
/* 15 */   WAXING_GIBBOUS(7, "waxing_gibbous");
/*    */   
/*    */   static  {
/* 18 */     CODEC = StringRepresentable.fromEnum(MoonPhase::values);
/*    */     
/* 20 */     COUNT = values().length;
/*    */   }
/*    */   public static final int PHASE_LENGTH = 24000;
/*    */   private final int index;
/*    */   private final String name;
/*    */   
/*    */   MoonPhase(int index, String name) {
/* 27 */     this.index = index;
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public int index() { return this.index; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int startTick() { return this.index * 24000; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\MoonPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */