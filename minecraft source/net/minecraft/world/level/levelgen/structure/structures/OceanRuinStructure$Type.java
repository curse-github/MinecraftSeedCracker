/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */ public static enum Type
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final Codec<Type> CODEC;
/* 53 */   WARM("warm"),
/* 54 */   COLD("cold");
/*    */   
/*    */   static  {
/* 57 */     CODEC = StringRepresentable.fromEnum(Type::values);
/*    */ 
/*    */     
/* 60 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Type::valueOf);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   Type(String name) { this.name = name; }
/*    */   @Deprecated
/*    */   public static final Codec<Type> LEGACY_CODEC; private final String name;
/*    */   
/* 69 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinStructure$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */