/*    */ package net.minecraft.world.entity.variant;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public final class BiomeCheck extends Record implements SpawnCondition {
/* 10 */   public BiomeCheck(HolderSet<Biome> requiredBiomes) { this.requiredBiomes = requiredBiomes; } private final HolderSet<Biome> requiredBiomes; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/BiomeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck; } public HolderSet<Biome> requiredBiomes() { return this.requiredBiomes; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/BiomeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/BiomeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/BiomeCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 14 */   public static final MapCodec<BiomeCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(BiomeCheck::requiredBiomes))
/* 16 */       .apply(i, BiomeCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean test(SpawnContext context) { return this.requiredBiomes.contains(context.biome()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<BiomeCheck> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\BiomeCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */