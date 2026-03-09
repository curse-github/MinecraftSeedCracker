/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum LiquidSettings implements StringRepresentable {
/*  7 */   IGNORE_WATERLOGGING("ignore_waterlogging"),
/*  8 */   APPLY_WATERLOGGING("apply_waterlogging"); public static Codec<LiquidSettings> CODEC;
/*    */   static  {
/* 10 */     CODEC = StringRepresentable.fromValues(LiquidSettings::values);
/*    */   }
/*    */   private final String name;
/*    */   
/* 14 */   LiquidSettings(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LiquidSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */