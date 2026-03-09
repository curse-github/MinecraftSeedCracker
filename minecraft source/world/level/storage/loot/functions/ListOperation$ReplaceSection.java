/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReplaceSection
/*     */   extends Record
/*     */   implements ListOperation
/*     */ {
/*     */   private final int offset;
/*     */   private final Optional<Integer> size;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  85 */   public ReplaceSection(int offset, Optional<Integer> size) { this.offset = offset; this.size = size; } public int offset() { return this.offset; } public Optional<Integer> size() { return this.size; }
/*  86 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  88 */   public static final MapCodec<ReplaceSection> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/*  89 */         .optionalFieldOf("offset", Integer.valueOf(0)).forGetter(ReplaceSection::offset), ExtraCodecs.NON_NEGATIVE_INT
/*  90 */         .optionalFieldOf("size").forGetter(ReplaceSection::size))
/*  91 */       .apply(i, ReplaceSection::new));
/*     */ 
/*     */   
/*  94 */   public ReplaceSection(int offset) { this(offset, Optional.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public ListOperation.Type mode() { return ListOperation.Type.REPLACE_SECTION; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 104 */     int originalSize = original.size();
/* 105 */     if (this.offset > originalSize) {
/* 106 */       LOGGER.error("Cannot replace when offset is out of bounds");
/* 107 */       return original;
/*     */     } 
/*     */     
/* 110 */     ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 111 */     newList.addAll(original.subList(0, this.offset));
/* 112 */     newList.addAll(replacement);
/* 113 */     int resumeIndex = this.offset + ((Integer)this.size.orElse(Integer.valueOf(replacement.size()))).intValue();
/* 114 */     if (resumeIndex < originalSize) {
/* 115 */       newList.addAll(original.subList(resumeIndex, originalSize));
/*     */     }
/* 117 */     ImmutableList immutableList = newList.build();
/* 118 */     if (immutableList.size() > maxSize) {
/* 119 */       LOGGER.error("Contents overflow in section replacement");
/* 120 */       return original;
/*     */     } 
/* 122 */     return immutableList;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$ReplaceSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */