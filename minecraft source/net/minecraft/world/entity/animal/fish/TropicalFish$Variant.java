/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.DyeColor;
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
/*    */ public final class Variant
/*    */   extends Record
/*    */ {
/*    */   private final TropicalFish.Pattern pattern;
/*    */   private final DyeColor baseColor;
/*    */   private final DyeColor patternColor;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 70 */   public Variant(TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) { this.pattern = pattern; this.baseColor = baseColor; this.patternColor = patternColor; } public TropicalFish.Pattern pattern() { return this.pattern; } public DyeColor baseColor() { return this.baseColor; } public DyeColor patternColor() { return this.patternColor; }
/* 71 */   public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::getPackedId);
/*    */ 
/*    */   
/* 74 */   public Variant(int packedId) { this(TropicalFish.getPattern(packedId), TropicalFish.getBaseColor(packedId), TropicalFish.getPatternColor(packedId)); }
/*    */ 
/*    */ 
/*    */   
/* 78 */   public int getPackedId() { return TropicalFish.packVariant(this.pattern, this.baseColor, this.patternColor); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\TropicalFish$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */