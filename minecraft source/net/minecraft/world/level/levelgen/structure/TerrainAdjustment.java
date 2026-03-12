/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ public static enum TerrainAdjustment
/*    */   implements StringRepresentable
/*    */ {
/* 10 */   NONE("none"),
/* 11 */   BURY("bury"),
/*    */   
/* 13 */   BEARD_THIN("beard_thin"),
/* 14 */   BEARD_BOX("beard_box"),
/* 15 */   ENCAPSULATE("encapsulate");
/*    */   
/*    */   static  {
/* 18 */     CODEC = StringRepresentable.fromEnum(TerrainAdjustment::values);
/*    */   }
/*    */   public static final Codec<TerrainAdjustment> CODEC;
/*    */   private final String id;
/*    */   
/* 23 */   TerrainAdjustment(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\TerrainAdjustment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */