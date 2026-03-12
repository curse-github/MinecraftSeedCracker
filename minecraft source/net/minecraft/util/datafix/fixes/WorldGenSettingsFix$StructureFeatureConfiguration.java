/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class StructureFeatureConfiguration
/*    */ {
/* 81 */   public static final Codec<StructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 82 */         .fieldOf("spacing").forGetter(()), Codec.INT
/* 83 */         .fieldOf("separation").forGetter(()), Codec.INT
/* 84 */         .fieldOf("salt").forGetter(()))
/* 85 */       .apply(i, StructureFeatureConfiguration::new));
/*    */   
/*    */   private final int spacing;
/*    */   private final int separation;
/*    */   private final int salt;
/*    */   
/*    */   public StructureFeatureConfiguration(int spacing, int separation, int salt) {
/* 92 */     this.spacing = spacing;
/* 93 */     this.separation = separation;
/* 94 */     this.salt = salt;
/*    */   }
/*    */ 
/*    */   
/* 98 */   public <T> Dynamic<T> serialize(DynamicOps<T> ops) { return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix$StructureFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */