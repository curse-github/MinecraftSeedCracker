/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
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
/*    */ public static enum JointType
/*    */   implements StringRepresentable
/*    */ {
/* 34 */   ROLLABLE("rollable"),
/* 35 */   ALIGNED("aligned");
/*    */   static  {
/* 37 */     CODEC = StringRepresentable.fromEnum(JointType::values);
/*    */   }
/*    */   public static final StringRepresentable.EnumCodec<JointType> CODEC;
/*    */   private final String name;
/*    */   
/* 42 */   JointType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Component getTranslatedName() { return Component.translatable("jigsaw_block.joint." + this.name); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\JigsawBlockEntity$JointType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */