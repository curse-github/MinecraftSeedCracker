/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */ public final class Setup
/*    */   extends Record
/*    */ {
/*    */   private final RuinedPortalPiece.VerticalPlacement placement;
/*    */   private final float airPocketProbability;
/*    */   private final float mossiness;
/*    */   private final boolean overgrown;
/*    */   private final boolean vines;
/*    */   private final boolean canBeCold;
/*    */   private final boolean replaceWithBlackstone;
/*    */   private final float weight;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 47 */   public Setup(RuinedPortalPiece.VerticalPlacement placement, float airPocketProbability, float mossiness, boolean overgrown, boolean vines, boolean canBeCold, boolean replaceWithBlackstone, float weight) { this.placement = placement; this.airPocketProbability = airPocketProbability; this.mossiness = mossiness; this.overgrown = overgrown; this.vines = vines; this.canBeCold = canBeCold; this.replaceWithBlackstone = replaceWithBlackstone; this.weight = weight; } public RuinedPortalPiece.VerticalPlacement placement() { return this.placement; } public float airPocketProbability() { return this.airPocketProbability; } public float mossiness() { return this.mossiness; } public boolean overgrown() { return this.overgrown; } public boolean vines() { return this.vines; } public boolean canBeCold() { return this.canBeCold; } public boolean replaceWithBlackstone() { return this.replaceWithBlackstone; } public float weight() { return this.weight; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public static final Codec<Setup> CODEC = RecordCodecBuilder.create(i -> i.group(RuinedPortalPiece.VerticalPlacement.CODEC
/* 58 */         .fieldOf("placement").forGetter(Setup::placement), 
/* 59 */         Codec.floatRange(0.0F, 1.0F).fieldOf("air_pocket_probability").forGetter(Setup::airPocketProbability), 
/* 60 */         Codec.floatRange(0.0F, 1.0F).fieldOf("mossiness").forGetter(Setup::mossiness), Codec.BOOL
/* 61 */         .fieldOf("overgrown").forGetter(Setup::overgrown), Codec.BOOL
/* 62 */         .fieldOf("vines").forGetter(Setup::vines), Codec.BOOL
/* 63 */         .fieldOf("can_be_cold").forGetter(Setup::canBeCold), Codec.BOOL
/* 64 */         .fieldOf("replace_with_blackstone").forGetter(Setup::replaceWithBlackstone), ExtraCodecs.POSITIVE_FLOAT
/* 65 */         .fieldOf("weight").forGetter(Setup::weight))
/* 66 */       .apply(i, Setup::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalStructure$Setup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */