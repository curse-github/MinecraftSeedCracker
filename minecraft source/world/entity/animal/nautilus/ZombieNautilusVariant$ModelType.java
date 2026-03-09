/*    */ package net.minecraft.world.entity.animal.nautilus;
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
/*    */ public static enum ModelType
/*    */   implements StringRepresentable
/*    */ {
/* 46 */   NORMAL("normal"),
/* 47 */   WARM("warm");
/*    */   static  {
/* 49 */     CODEC = StringRepresentable.fromEnum(ModelType::values);
/*    */   }
/*    */   public static final Codec<ModelType> CODEC;
/*    */   private final String name;
/*    */   
/* 54 */   ModelType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusVariant$ModelType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */