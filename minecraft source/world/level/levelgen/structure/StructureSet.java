/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*    */ 
/*    */ public final class StructureSet extends Record {
/*    */   private final List<StructureSelectionEntry> structures;
/*    */   private final StructurePlacement placement;
/*    */   
/* 16 */   public StructureSet(List<StructureSelectionEntry> structures, StructurePlacement placement) { this.structures = structures; this.placement = placement; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet; } public List<StructureSelectionEntry> structures() { return this.structures; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public StructurePlacement placement() { return this.placement; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final Codec<StructureSet> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(StructureSelectionEntry.CODEC
/* 21 */         .listOf().fieldOf("structures").forGetter(StructureSet::structures), StructurePlacement.CODEC
/* 22 */         .fieldOf("placement").forGetter(StructureSet::placement))
/* 23 */       .apply(i, StructureSet::new));
/* 24 */   public static final Codec<Holder<StructureSet>> CODEC = RegistryFileCodec.create(Registries.STRUCTURE_SET, DIRECT_CODEC);
/*    */ 
/*    */   
/* 27 */   public StructureSet(Holder<Structure> singleEntry, StructurePlacement placement) { this(List.of(new StructureSelectionEntry(singleEntry, 1)), placement); }
/*    */   public static final class StructureSelectionEntry extends Record { private final Holder<Structure> structure; private final int weight;
/*    */     
/* 30 */     public StructureSelectionEntry(Holder<Structure> structure, int weight) { this.structure = structure; this.weight = weight; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;
/* 30 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Structure> structure() { return this.structure; } public int weight() { return this.weight; }
/*    */ 
/*    */ 
/*    */     
/* 34 */     public static final Codec<StructureSelectionEntry> CODEC = RecordCodecBuilder.create(i -> i.group(Structure.CODEC
/* 35 */           .fieldOf("structure").forGetter(StructureSelectionEntry::structure), ExtraCodecs.POSITIVE_INT
/* 36 */           .fieldOf("weight").forGetter(StructureSelectionEntry::weight))
/* 37 */         .apply(i, StructureSelectionEntry::new)); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static StructureSelectionEntry entry(Holder<Structure> structure, int weight) { return new StructureSelectionEntry(structure, weight); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static StructureSelectionEntry entry(Holder<Structure> structure) { return new StructureSelectionEntry(structure, 1); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */