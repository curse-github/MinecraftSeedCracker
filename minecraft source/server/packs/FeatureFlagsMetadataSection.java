/*    */ package net.minecraft.server.packs;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class FeatureFlagsMetadataSection extends Record {
/*    */   private final FeatureFlagSet flags;
/*    */   
/*  9 */   public FeatureFlagsMetadataSection(FeatureFlagSet flags) { this.flags = flags; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection; } public FeatureFlagSet flags() { return this.flags; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   private static final Codec<FeatureFlagsMetadataSection> CODEC = RecordCodecBuilder.create(i -> i.group(FeatureFlags.CODEC
/* 11 */         .fieldOf("enabled").forGetter(FeatureFlagsMetadataSection::flags))
/* 12 */       .apply(i, FeatureFlagsMetadataSection::new));
/*    */   
/* 14 */   public static final MetadataSectionType<FeatureFlagsMetadataSection> TYPE = new MetadataSectionType("features", CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\FeatureFlagsMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */