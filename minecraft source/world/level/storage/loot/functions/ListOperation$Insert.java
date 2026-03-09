/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
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
/*     */ public final class Insert
/*     */   extends Record
/*     */   implements ListOperation
/*     */ {
/*     */   private final int offset;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 126 */   public Insert(int offset) { this.offset = offset; } public int offset() { return this.offset; }
/* 127 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 129 */   public static final MapCodec<Insert> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 130 */         .optionalFieldOf("offset", Integer.valueOf(0)).forGetter(Insert::offset))
/* 131 */       .apply(i, Insert::new));
/*     */ 
/*     */ 
/*     */   
/* 135 */   public ListOperation.Type mode() { return ListOperation.Type.INSERT; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 140 */     int originalSize = original.size();
/* 141 */     if (this.offset > originalSize) {
/* 142 */       LOGGER.error("Cannot insert when offset is out of bounds");
/* 143 */       return original;
/*     */     } 
/* 145 */     if (originalSize + replacement.size() > maxSize) {
/* 146 */       LOGGER.error("Contents overflow in section insertion");
/* 147 */       return original;
/*     */     } 
/*     */     
/* 150 */     ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 151 */     newList.addAll(original.subList(0, this.offset));
/* 152 */     newList.addAll(replacement);
/* 153 */     newList.addAll(original.subList(this.offset, originalSize));
/* 154 */     return newList.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$Insert.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */