/*    */ package net.minecraft.world.level.levelgen.structure;
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
/*    */ public static enum BoundingBoxType
/*    */   implements StringRepresentable
/*    */ {
/* 19 */   PIECE("piece"),
/* 20 */   STRUCTURE("full");
/*    */   static  {
/* 22 */     CODEC = StringRepresentable.fromEnum(BoundingBoxType::values);
/*    */   }
/*    */   public static final Codec<BoundingBoxType> CODEC;
/*    */   private final String id;
/*    */   
/* 27 */   BoundingBoxType(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureSpawnOverride$BoundingBoxType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */