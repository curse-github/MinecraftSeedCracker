/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class FlatLevelGeneratorPreset extends Record {
/*    */   private final Holder<Item> displayItem;
/*    */   private final FlatLevelGeneratorSettings settings;
/*    */   
/* 10 */   public FlatLevelGeneratorPreset(Holder<Item> displayItem, FlatLevelGeneratorSettings settings) { this.displayItem = displayItem; this.settings = settings; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset; } public Holder<Item> displayItem() { return this.displayItem; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public FlatLevelGeneratorSettings settings() { return this.settings; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Codec<FlatLevelGeneratorPreset> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Item.CODEC
/* 15 */         .fieldOf("display").forGetter(()), FlatLevelGeneratorSettings.CODEC
/* 16 */         .fieldOf("settings").forGetter(()))
/* 17 */       .apply(i, FlatLevelGeneratorPreset::new));
/*    */   
/* 19 */   public static final Codec<Holder<FlatLevelGeneratorPreset>> CODEC = RegistryFileCodec.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, DIRECT_CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */