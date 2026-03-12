/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface ListOperation {
/*  18 */   public static final MapCodec<ListOperation> UNLIMITED_CODEC = codec(2147483647);
/*     */   
/*     */   static MapCodec<ListOperation> codec(int maxSize) {
/*  21 */     return Type.CODEC.dispatchMap("mode", ListOperation::mode, e -> e.mapCodec).validate(op -> {
/*     */           
/*  23 */           if (op instanceof ReplaceSection) { ReplaceSection section = (ReplaceSection)op; if (section.size().isPresent()) {
/*  24 */               int size = ((Integer)section.size().get()).intValue();
/*  25 */               if (size > maxSize)
/*  26 */                 return DataResult.error(()); 
/*     */             }  }
/*     */           
/*  29 */           return DataResult.success(op);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   default <T> List<T> apply(List<T> original, List<T> replacement) { return apply(original, replacement, 2147483647); }
/*     */   
/*     */   Type mode();
/*     */   
/*     */   <T> List<T> apply(List<T> paramList1, List<T> paramList2, int paramInt);
/*     */   
/*  42 */   public enum Type implements StringRepresentable { REPLACE_ALL("replace_all", ListOperation.ReplaceAll.MAP_CODEC),
/*  43 */     REPLACE_SECTION("replace_section", ListOperation.ReplaceSection.MAP_CODEC),
/*  44 */     INSERT("insert", ListOperation.Insert.MAP_CODEC),
/*  45 */     APPEND("append", ListOperation.Append.MAP_CODEC); public static final Codec<Type> CODEC; private final String id; private final MapCodec<? extends ListOperation> mapCodec;
/*     */     static  {
/*  47 */       CODEC = StringRepresentable.fromEnum(Type::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Type(String id, MapCodec<? extends ListOperation> mapCodec) {
/*  53 */       this.id = id;
/*  54 */       this.mapCodec = mapCodec;
/*     */     }
/*     */ 
/*     */     
/*  58 */     public MapCodec<? extends ListOperation> mapCodec() { return this.mapCodec; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     public String getSerializedName() { return this.id; } }
/*     */ 
/*     */   
/*     */   public static class ReplaceAll
/*     */     implements ListOperation {
/*  68 */     public static final ReplaceAll INSTANCE = new ReplaceAll();
/*  69 */     public static final MapCodec<ReplaceAll> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     public ListOperation.Type mode() { return ListOperation.Type.REPLACE_ALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) { return replacement; } }
/*     */   public static final class ReplaceSection extends Record implements ListOperation { private final int offset;
/*     */     private final Optional<Integer> size;
/*     */     
/*  85 */     public ReplaceSection(int offset, Optional<Integer> size) { this.offset = offset; this.size = size; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  85 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection; } public int offset() { return this.offset; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*  85 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> size() { return this.size; }
/*  86 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*  88 */     public static final MapCodec<ReplaceSection> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/*  89 */           .optionalFieldOf("offset", Integer.valueOf(0)).forGetter(ReplaceSection::offset), ExtraCodecs.NON_NEGATIVE_INT
/*  90 */           .optionalFieldOf("size").forGetter(ReplaceSection::size))
/*  91 */         .apply(i, ReplaceSection::new));
/*     */ 
/*     */     
/*  94 */     public ReplaceSection(int offset) { this(offset, Optional.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     public ListOperation.Type mode() { return ListOperation.Type.REPLACE_SECTION; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 104 */       int originalSize = original.size();
/* 105 */       if (this.offset > originalSize) {
/* 106 */         LOGGER.error("Cannot replace when offset is out of bounds");
/* 107 */         return original;
/*     */       } 
/*     */       
/* 110 */       ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 111 */       newList.addAll(original.subList(0, this.offset));
/* 112 */       newList.addAll(replacement);
/* 113 */       int resumeIndex = this.offset + ((Integer)this.size.orElse(Integer.valueOf(replacement.size()))).intValue();
/* 114 */       if (resumeIndex < originalSize) {
/* 115 */         newList.addAll(original.subList(resumeIndex, originalSize));
/*     */       }
/* 117 */       ImmutableList immutableList = newList.build();
/* 118 */       if (immutableList.size() > maxSize) {
/* 119 */         LOGGER.error("Contents overflow in section replacement");
/* 120 */         return original;
/*     */       } 
/* 122 */       return immutableList;
/*     */     } }
/*     */   public static final class Insert extends Record implements ListOperation { private final int offset;
/*     */     
/* 126 */     public Insert(int offset) { this.offset = offset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;
/* 126 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; }
/* 127 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/* 129 */     public static final MapCodec<Insert> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 130 */           .optionalFieldOf("offset", Integer.valueOf(0)).forGetter(Insert::offset))
/* 131 */         .apply(i, Insert::new));
/*     */ 
/*     */ 
/*     */     
/* 135 */     public ListOperation.Type mode() { return ListOperation.Type.INSERT; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 140 */       int originalSize = original.size();
/* 141 */       if (this.offset > originalSize) {
/* 142 */         LOGGER.error("Cannot insert when offset is out of bounds");
/* 143 */         return original;
/*     */       } 
/* 145 */       if (originalSize + replacement.size() > maxSize) {
/* 146 */         LOGGER.error("Contents overflow in section insertion");
/* 147 */         return original;
/*     */       } 
/*     */       
/* 150 */       ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 151 */       newList.addAll(original.subList(0, this.offset));
/* 152 */       newList.addAll(replacement);
/* 153 */       newList.addAll(original.subList(this.offset, originalSize));
/* 154 */       return newList.build();
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Append implements ListOperation {
/* 159 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/* 161 */     public static final Append INSTANCE = new Append();
/* 162 */     public static final MapCodec<Append> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     public ListOperation.Type mode() { return ListOperation.Type.APPEND; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 174 */       if (original.size() + replacement.size() > maxSize) {
/* 175 */         LOGGER.error("Contents overflow in section append");
/* 176 */         return original;
/*     */       } 
/* 178 */       return Stream.concat(original.stream(), replacement.stream()).toList();
/*     */     } }
/*     */   public static final class StandAlone<T> extends Record { private final List<T> value; private final ListOperation operation;
/*     */     
/* 182 */     public StandAlone(List<T> value, ListOperation operation) { this.value = value; this.operation = operation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 182 */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>; } public List<T> value() { return this.value; } public ListOperation operation() { return this.operation; }
/*     */     
/* 184 */     public static <T> Codec<StandAlone<T>> codec(Codec<T> valueCodec, int maxSize) { return RecordCodecBuilder.create(i -> i.group(valueCodec
/* 185 */             .sizeLimitedListOf(maxSize).fieldOf("values").forGetter(()), 
/* 186 */             ListOperation.codec(maxSize).forGetter(()))
/* 187 */           .apply(i, StandAlone::new)); }
/*     */ 
/*     */ 
/*     */     
/* 191 */     public List<T> apply(List<T> input) { return this.operation.apply(input, this.value); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */