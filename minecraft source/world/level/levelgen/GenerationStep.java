/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public class GenerationStep
/*    */ {
/*    */   public enum Decoration
/*    */     implements StringRepresentable {
/* 10 */     RAW_GENERATION("raw_generation"),
/*    */     
/* 12 */     LAKES("lakes"),
/*    */     
/* 14 */     LOCAL_MODIFICATIONS("local_modifications"),
/*    */     
/* 16 */     UNDERGROUND_STRUCTURES("underground_structures"),
/*    */     
/* 18 */     SURFACE_STRUCTURES("surface_structures"),
/*    */     
/* 20 */     STRONGHOLDS("strongholds"),
/*    */     
/* 22 */     UNDERGROUND_ORES("underground_ores"),
/*    */     
/* 24 */     UNDERGROUND_DECORATION("underground_decoration"),
/*    */     
/* 26 */     FLUID_SPRINGS("fluid_springs"),
/*    */     
/* 28 */     VEGETAL_DECORATION("vegetal_decoration"),
/*    */     
/* 30 */     TOP_LAYER_MODIFICATION("top_layer_modification");
/*    */     
/*    */     static  {
/* 33 */       CODEC = StringRepresentable.fromEnum(Decoration::values);
/*    */     }
/*    */     public static final Codec<Decoration> CODEC;
/*    */     private final String name;
/*    */     
/* 38 */     Decoration(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */     
/* 42 */     public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\GenerationStep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */