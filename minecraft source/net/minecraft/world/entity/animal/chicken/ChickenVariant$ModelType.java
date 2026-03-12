/*    */ package net.minecraft.world.entity.animal.chicken;
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
/*    */ public static enum ModelType
/*    */   implements StringRepresentable
/*    */ {
/* 48 */   NORMAL("normal"),
/* 49 */   COLD("cold");
/*    */   static  {
/* 51 */     CODEC = StringRepresentable.fromEnum(ModelType::values);
/*    */   }
/*    */   public static final Codec<ModelType> CODEC;
/*    */   private final String name;
/*    */   
/* 56 */   ModelType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\chicken\ChickenVariant$ModelType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */