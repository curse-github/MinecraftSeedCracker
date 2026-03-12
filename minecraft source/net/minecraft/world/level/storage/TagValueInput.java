/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntArrayTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NumericTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagType;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public class TagValueInput
/*     */   implements ValueInput
/*     */ {
/*     */   private final ProblemReporter problemReporter;
/*     */   private final ValueInputContextHelper context;
/*     */   private final CompoundTag input;
/*     */   
/*     */   private TagValueInput(ProblemReporter problemReporter, ValueInputContextHelper context, CompoundTag input) {
/*  35 */     this.problemReporter = problemReporter;
/*  36 */     this.context = context;
/*  37 */     this.input = input;
/*     */   }
/*     */ 
/*     */   
/*  41 */   public static ValueInput create(ProblemReporter problemReporter, HolderLookup.Provider holders, CompoundTag tag) { return new TagValueInput(problemReporter, new ValueInputContextHelper(holders, NbtOps.INSTANCE), tag); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static ValueInput.ValueInputList create(ProblemReporter problemReporter, HolderLookup.Provider holders, List<CompoundTag> tags) { return new CompoundListWrapper(problemReporter, new ValueInputContextHelper(holders, NbtOps.INSTANCE), tags); }
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
/*     */   public <T> Optional<T> read(String name, Codec<T> codec) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield input : Lnet/minecraft/nbt/CompoundTag;
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual get : (Ljava/lang/String;)Lnet/minecraft/nbt/Tag;
/*     */     //   8: astore_3
/*     */     //   9: aload_3
/*     */     //   10: ifnonnull -> 17
/*     */     //   13: invokestatic empty : ()Ljava/util/Optional;
/*     */     //   16: areturn
/*     */     //   17: aload_2
/*     */     //   18: aload_0
/*     */     //   19: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */     //   22: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */     //   25: aload_3
/*     */     //   26: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   31: dup
/*     */     //   32: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   35: pop
/*     */     //   36: astore #4
/*     */     //   38: iconst_0
/*     */     //   39: istore #5
/*     */     //   41: aload #4
/*     */     //   43: iload #5
/*     */     //   45: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   50: lookupswitch default -> 76, 0 -> 86, 1 -> 104
/*     */     //   76: new java/lang/MatchException
/*     */     //   79: dup
/*     */     //   80: aconst_null
/*     */     //   81: aconst_null
/*     */     //   82: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   85: athrow
/*     */     //   86: aload #4
/*     */     //   88: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   91: astore #6
/*     */     //   93: aload #6
/*     */     //   95: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   98: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   101: goto -> 136
/*     */     //   104: aload #4
/*     */     //   106: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   109: astore #7
/*     */     //   111: aload_0
/*     */     //   112: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */     //   115: new net/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem
/*     */     //   118: dup
/*     */     //   119: aload_1
/*     */     //   120: aload_3
/*     */     //   121: aload #7
/*     */     //   123: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */     //   126: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */     //   131: aload #7
/*     */     //   133: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */     //   136: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #50	-> 0
/*     */     //   #51	-> 9
/*     */     //   #52	-> 13
/*     */     //   #55	-> 17
/*     */     //   #56	-> 86
/*     */     //   #57	-> 104
/*     */     //   #58	-> 111
/*     */     //   #59	-> 131
/*     */     //   #55	-> 136
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   93	11	6	success	Lcom/mojang/serialization/DataResult$Success;
/*     */     //   111	25	7	error	Lcom/mojang/serialization/DataResult$Error;
/*     */     //   0	137	0	this	Lnet/minecraft/world/level/storage/TagValueInput;
/*     */     //   0	137	1	name	Ljava/lang/String;
/*     */     //   0	137	2	codec	Lcom/mojang/serialization/Codec;
/*     */     //   9	128	3	tag	Lnet/minecraft/nbt/Tag;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   93	11	6	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */     //   111	25	7	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */     //   0	137	2	codec	Lcom/mojang/serialization/Codec<TT;>; }
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
/*     */   public <T> Optional<T> read(MapCodec<T> codec) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */     //   4: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */     //   7: astore_2
/*     */     //   8: aload_2
/*     */     //   9: aload_0
/*     */     //   10: getfield input : Lnet/minecraft/nbt/CompoundTag;
/*     */     //   13: invokeinterface getMap : (Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   18: aload_1
/*     */     //   19: aload_2
/*     */     //   20: <illegal opcode> apply : (Lcom/mojang/serialization/MapCodec;Lcom/mojang/serialization/DynamicOps;)Ljava/util/function/Function;
/*     */     //   25: invokeinterface flatMap : (Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;
/*     */     //   30: dup
/*     */     //   31: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   34: pop
/*     */     //   35: astore_3
/*     */     //   36: iconst_0
/*     */     //   37: istore #4
/*     */     //   39: aload_3
/*     */     //   40: iload #4
/*     */     //   42: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   47: lookupswitch default -> 72, 0 -> 82, 1 -> 99
/*     */     //   72: new java/lang/MatchException
/*     */     //   75: dup
/*     */     //   76: aconst_null
/*     */     //   77: aconst_null
/*     */     //   78: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   81: athrow
/*     */     //   82: aload_3
/*     */     //   83: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   86: astore #5
/*     */     //   88: aload #5
/*     */     //   90: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   93: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   96: goto -> 128
/*     */     //   99: aload_3
/*     */     //   100: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   103: astore #6
/*     */     //   105: aload_0
/*     */     //   106: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */     //   109: new net/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem
/*     */     //   112: dup
/*     */     //   113: aload #6
/*     */     //   115: invokespecial <init> : (Lcom/mojang/serialization/DataResult$Error;)V
/*     */     //   118: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */     //   123: aload #6
/*     */     //   125: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */     //   128: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     //   #67	-> 8
/*     */     //   #68	-> 82
/*     */     //   #69	-> 99
/*     */     //   #70	-> 105
/*     */     //   #71	-> 123
/*     */     //   #67	-> 128
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   88	11	5	success	Lcom/mojang/serialization/DataResult$Success;
/*     */     //   105	23	6	error	Lcom/mojang/serialization/DataResult$Error;
/*     */     //   0	129	0	this	Lnet/minecraft/world/level/storage/TagValueInput;
/*     */     //   0	129	1	codec	Lcom/mojang/serialization/MapCodec;
/*     */     //   8	121	2	ops	Lcom/mojang/serialization/DynamicOps;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   88	11	5	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */     //   105	23	6	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */     //   0	129	1	codec	Lcom/mojang/serialization/MapCodec<TT;>;
/*     */     //   8	121	2	ops	Lcom/mojang/serialization/DynamicOps<Lnet/minecraft/nbt/Tag;>; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends Tag> T getOptionalTypedTag(String name, TagType<T> expectedType) {
/*  78 */     Tag tag = this.input.get(name);
/*  79 */     if (tag == null) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     TagType<?> actualType = tag.getType();
/*  84 */     if (actualType != expectedType) {
/*  85 */       this.problemReporter.report(new UnexpectedTypeProblem(name, expectedType, actualType));
/*  86 */       return null;
/*     */     } 
/*  88 */     return (T)tag;
/*     */   }
/*     */   
/*     */   private NumericTag getNumericTag(String name) {
/*  92 */     Tag tag = this.input.get(name);
/*  93 */     if (tag == null) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     if (tag instanceof NumericTag) return (NumericTag)tag;
/*     */ 
/*     */ 
/*     */     
/* 101 */     this.problemReporter.report(new UnexpectedNonNumberProblem(name, tag.getType()));
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ValueInput> child(String name) {
/* 107 */     CompoundTag compound = (CompoundTag)getOptionalTypedTag(name, CompoundTag.TYPE);
/* 108 */     return (compound != null) ? Optional.of(wrapChild(name, compound)) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueInput childOrEmpty(String name) {
/* 113 */     CompoundTag compound = (CompoundTag)getOptionalTypedTag(name, CompoundTag.TYPE);
/* 114 */     return (compound != null) ? wrapChild(name, compound) : this.context.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ValueInput.ValueInputList> childrenList(String name) {
/* 119 */     ListTag list = (ListTag)getOptionalTypedTag(name, ListTag.TYPE);
/* 120 */     return (list != null) ? Optional.of(wrapList(name, this.context, list)) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueInput.ValueInputList childrenListOrEmpty(String name) {
/* 125 */     ListTag list = (ListTag)getOptionalTypedTag(name, ListTag.TYPE);
/* 126 */     return (list != null) ? wrapList(name, this.context, list) : this.context.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<ValueInput.TypedInputList<T>> list(String name, Codec<T> codec) {
/* 131 */     ListTag list = (ListTag)getOptionalTypedTag(name, ListTag.TYPE);
/* 132 */     return (list != null) ? Optional.of(wrapTypedList(name, list, codec)) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ValueInput.TypedInputList<T> listOrEmpty(String name, Codec<T> codec) {
/* 137 */     ListTag list = (ListTag)getOptionalTypedTag(name, ListTag.TYPE);
/* 138 */     return (list != null) ? wrapTypedList(name, list, codec) : this.context.emptyTypedList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBooleanOr(String name, boolean defaultValue) {
/* 143 */     NumericTag numericTag = getNumericTag(name);
/* 144 */     return (numericTag != null) ? ((numericTag.byteValue() != 0)) : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByteOr(String name, byte defaultValue) {
/* 149 */     NumericTag numericTag = getNumericTag(name);
/* 150 */     return (numericTag != null) ? numericTag.byteValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getShortOr(String name, short defaultValue) {
/* 155 */     NumericTag numericTag = getNumericTag(name);
/* 156 */     return (numericTag != null) ? numericTag.shortValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Integer> getInt(String name) {
/* 161 */     NumericTag numericTag = getNumericTag(name);
/* 162 */     return (numericTag != null) ? Optional.of(Integer.valueOf(numericTag.intValue())) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntOr(String name, int defaultValue) {
/* 167 */     NumericTag numericTag = getNumericTag(name);
/* 168 */     return (numericTag != null) ? numericTag.intValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongOr(String name, long defaultValue) {
/* 173 */     NumericTag numericTag = getNumericTag(name);
/* 174 */     return (numericTag != null) ? numericTag.longValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Long> getLong(String name) {
/* 179 */     NumericTag numericTag = getNumericTag(name);
/* 180 */     return (numericTag != null) ? Optional.of(Long.valueOf(numericTag.longValue())) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloatOr(String name, float defaultValue) {
/* 185 */     NumericTag numericTag = getNumericTag(name);
/* 186 */     return (numericTag != null) ? numericTag.floatValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDoubleOr(String name, double defaultValue) {
/* 191 */     NumericTag numericTag = getNumericTag(name);
/* 192 */     return (numericTag != null) ? numericTag.doubleValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> getString(String name) {
/* 197 */     StringTag tag = (StringTag)getOptionalTypedTag(name, StringTag.TYPE);
/* 198 */     return (tag != null) ? Optional.of(tag.value()) : Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStringOr(String name, String defaultValue) {
/* 203 */     StringTag tag = (StringTag)getOptionalTypedTag(name, StringTag.TYPE);
/* 204 */     return (tag != null) ? tag.value() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<int[]> getIntArray(String name) {
/* 209 */     IntArrayTag tag = (IntArrayTag)getOptionalTypedTag(name, IntArrayTag.TYPE);
/* 210 */     return (tag != null) ? Optional.of(tag.getAsIntArray()) : Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public HolderLookup.Provider lookup() { return this.context.lookup(); }
/*     */ 
/*     */   
/*     */   private ValueInput wrapChild(String name, CompoundTag compoundTag) {
/* 219 */     return compoundTag.isEmpty() ? 
/* 220 */       this.context.empty() : 
/* 221 */       new TagValueInput(this.problemReporter.forChild(new ProblemReporter.FieldPathElement(name)), this.context, compoundTag);
/*     */   }
/*     */ 
/*     */   
/* 225 */   private static ValueInput wrapChild(ProblemReporter problemReporter, ValueInputContextHelper context, CompoundTag compoundTag) { return compoundTag.isEmpty() ? context.empty() : new TagValueInput(problemReporter, context, compoundTag); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   private ValueInput.ValueInputList wrapList(String name, ValueInputContextHelper context, ListTag list) { return list.isEmpty() ? context.emptyList() : new ListWrapper(this.problemReporter, name, context, list); }
/*     */ 
/*     */ 
/*     */   
/* 233 */   private <T> ValueInput.TypedInputList<T> wrapTypedList(String name, ListTag list, Codec<T> codec) { return list.isEmpty() ? this.context.emptyTypedList() : new TypedListWrapper(this.problemReporter, name, this.context, codec, list); }
/*     */   
/*     */   private static class ListWrapper
/*     */     implements ValueInput.ValueInputList {
/*     */     private final ProblemReporter problemReporter;
/*     */     private final String name;
/*     */     private final ValueInputContextHelper context;
/*     */     private final ListTag list;
/*     */     
/*     */     private ListWrapper(ProblemReporter problemReporter, String name, ValueInputContextHelper context, ListTag list) {
/* 243 */       this.problemReporter = problemReporter;
/* 244 */       this.name = name;
/* 245 */       this.context = context;
/* 246 */       this.list = list;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     public boolean isEmpty() { return this.list.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/* 255 */     private ProblemReporter reporterForChild(int index) { return this.problemReporter.forChild(new ProblemReporter.IndexedFieldPathElement(this.name, index)); }
/*     */ 
/*     */ 
/*     */     
/* 259 */     private void reportIndexUnwrapProblem(int index, Tag value) { this.problemReporter.report(new TagValueInput.UnexpectedListElementTypeProblem(this.name, index, CompoundTag.TYPE, value.getType())); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<ValueInput> stream() {
/* 264 */       return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
/* 265 */             if (value instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)value;
/* 266 */               return TagValueInput.wrapChild(reporterForChild((int)index), this.context, compoundTag); }
/*     */             
/* 268 */             reportIndexUnwrapProblem((int)index, value);
/* 269 */             return null;
/*     */ 
/*     */           
/* 272 */           }).filter(Objects::nonNull);
/*     */     }
/*     */     
/*     */     public Iterator<ValueInput> iterator()
/*     */     {
/* 277 */       final Iterator<Tag> iterator = this.list.iterator();
/* 278 */       return new AbstractIterator<ValueInput>()
/*     */         {
/*     */           private int index;
/*     */           
/*     */           protected ValueInput computeNext() {
/* 283 */             while (iterator.hasNext()) {
/* 284 */               Tag value = (Tag)iterator.next();
/* 285 */               int currentIndex = this.index++;
/* 286 */               if (value instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)value;
/* 287 */                 return TagValueInput.wrapChild(TagValueInput.ListWrapper.this.reporterForChild(currentIndex), TagValueInput.ListWrapper.this.context, compoundTag); }
/*     */               
/* 289 */               TagValueInput.ListWrapper.this.reportIndexUnwrapProblem(currentIndex, value);
/*     */             } 
/*     */             
/* 292 */             return (ValueInput)endOfData(); } }; } } class null extends AbstractIterator<ValueInput> { protected ValueInput computeNext() { while (iterator.hasNext()) { Tag value = (Tag)iterator.next(); int currentIndex = this.index++; if (value instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)value; return TagValueInput.wrapChild(TagValueInput.ListWrapper.this.reporterForChild(currentIndex), TagValueInput.ListWrapper.this.context, compoundTag); }  TagValueInput.ListWrapper.this.reportIndexUnwrapProblem(currentIndex, value); }  return (ValueInput)endOfData(); }
/*     */ 
/*     */     
/*     */     private int index; }
/*     */ 
/*     */   
/*     */   private static class TypedListWrapper<T> extends Object implements ValueInput.TypedInputList<T> {
/*     */     private final ProblemReporter problemReporter;
/*     */     private final String name;
/*     */     private final ValueInputContextHelper context;
/*     */     private final Codec<T> codec;
/*     */     private final ListTag list;
/*     */     
/*     */     private TypedListWrapper(ProblemReporter problemReporter, String name, ValueInputContextHelper context, Codec<T> codec, ListTag list) {
/* 306 */       this.problemReporter = problemReporter;
/* 307 */       this.name = name;
/* 308 */       this.context = context;
/* 309 */       this.codec = codec;
/* 310 */       this.list = list;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 315 */     public boolean isEmpty() { return this.list.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/* 319 */     private void reportIndexUnwrapProblem(int index, Tag value, DataResult.Error<?> error) { this.problemReporter.report(new TagValueInput.DecodeFromListFailedProblem(this.name, index, value, error)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<T> stream() {
/* 324 */       return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: getfield codec : Lcom/mojang/serialization/Codec;
/*     */             //   4: aload_0
/*     */             //   5: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */             //   8: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */             //   11: aload_1
/*     */             //   12: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */             //   17: dup
/*     */             //   18: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   21: pop
/*     */             //   22: astore #4
/*     */             //   24: iconst_0
/*     */             //   25: istore #5
/*     */             //   27: aload #4
/*     */             //   29: iload #5
/*     */             //   31: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */             //   36: lookupswitch default -> 64, 0 -> 74, 1 -> 92
/*     */             //   64: new java/lang/MatchException
/*     */             //   67: dup
/*     */             //   68: aconst_null
/*     */             //   69: aconst_null
/*     */             //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */             //   73: athrow
/*     */             //   74: aload #4
/*     */             //   76: checkcast com/mojang/serialization/DataResult$Success
/*     */             //   79: astore #6
/*     */             //   81: aload #6
/*     */             //   83: invokevirtual value : ()Ljava/lang/Object;
/*     */             //   86: checkcast java/lang/Object
/*     */             //   89: goto -> 120
/*     */             //   92: aload #4
/*     */             //   94: checkcast com/mojang/serialization/DataResult$Error
/*     */             //   97: astore #7
/*     */             //   99: aload_0
/*     */             //   100: lload_2
/*     */             //   101: l2i
/*     */             //   102: aload_1
/*     */             //   103: aload #7
/*     */             //   105: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */             //   108: aload #7
/*     */             //   110: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */             //   113: aconst_null
/*     */             //   114: invokevirtual orElse : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   117: checkcast java/lang/Object
/*     */             //   120: areturn
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #325	-> 0
/*     */             //   #326	-> 74
/*     */             //   #327	-> 92
/*     */             //   #328	-> 99
/*     */             //   #329	-> 108
/*     */             //   #327	-> 120
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   81	11	6	success	Lcom/mojang/serialization/DataResult$Success;
/*     */             //   99	21	7	error	Lcom/mojang/serialization/DataResult$Error;
/*     */             //   24	96	4	selector0$temp	Lcom/mojang/serialization/DataResult;
/*     */             //   27	93	5	index$1	I
/*     */             //   0	121	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */             //   0	121	1	value	Lnet/minecraft/nbt/Tag;
/*     */             //   0	121	2	index	J
/*     */             // Local variable type table:
/*     */             //   start	length	slot	name	signature
/*     */             //   81	11	6	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */             //   99	21	7	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */             //   0	121	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper<TT;>;
/* 332 */           }).filter(Objects::nonNull);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 337 */       final ListIterator<Tag> iterator = this.list.listIterator();
/* 338 */       return new AbstractIterator<T>()
/*     */         {
/*     */           protected T computeNext() { // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: getfield val$iterator : Ljava/util/ListIterator;
/*     */             //   4: invokeinterface hasNext : ()Z
/*     */             //   9: ifeq -> 162
/*     */             //   12: aload_0
/*     */             //   13: getfield val$iterator : Ljava/util/ListIterator;
/*     */             //   16: invokeinterface nextIndex : ()I
/*     */             //   21: istore_1
/*     */             //   22: aload_0
/*     */             //   23: getfield val$iterator : Ljava/util/ListIterator;
/*     */             //   26: invokeinterface next : ()Ljava/lang/Object;
/*     */             //   31: checkcast net/minecraft/nbt/Tag
/*     */             //   34: astore_2
/*     */             //   35: aload_0
/*     */             //   36: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */             //   39: getfield codec : Lcom/mojang/serialization/Codec;
/*     */             //   42: aload_0
/*     */             //   43: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */             //   46: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */             //   49: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */             //   52: aload_2
/*     */             //   53: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */             //   58: dup
/*     */             //   59: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   62: pop
/*     */             //   63: astore_3
/*     */             //   64: iconst_0
/*     */             //   65: istore #4
/*     */             //   67: aload_3
/*     */             //   68: iload #4
/*     */             //   70: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */             //   75: lookupswitch default -> 100, 0 -> 110, 1 -> 122
/*     */             //   100: new java/lang/MatchException
/*     */             //   103: dup
/*     */             //   104: aconst_null
/*     */             //   105: aconst_null
/*     */             //   106: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */             //   109: athrow
/*     */             //   110: aload_3
/*     */             //   111: checkcast com/mojang/serialization/DataResult$Success
/*     */             //   114: astore #5
/*     */             //   116: aload #5
/*     */             //   118: invokevirtual value : ()Ljava/lang/Object;
/*     */             //   121: areturn
/*     */             //   122: aload_3
/*     */             //   123: checkcast com/mojang/serialization/DataResult$Error
/*     */             //   126: astore #6
/*     */             //   128: aload_0
/*     */             //   129: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */             //   132: iload_1
/*     */             //   133: aload_2
/*     */             //   134: aload #6
/*     */             //   136: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */             //   139: aload #6
/*     */             //   141: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */             //   144: invokevirtual isPresent : ()Z
/*     */             //   147: ifeq -> 159
/*     */             //   150: aload #6
/*     */             //   152: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */             //   155: invokevirtual get : ()Ljava/lang/Object;
/*     */             //   158: areturn
/*     */             //   159: goto -> 0
/*     */             //   162: aload_0
/*     */             //   163: invokevirtual endOfData : ()Ljava/lang/Object;
/*     */             //   166: areturn
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #341	-> 0
/*     */             //   #342	-> 12
/*     */             //   #343	-> 22
/*     */             //   #344	-> 35
/*     */             //   #345	-> 110
/*     */             //   #346	-> 116
/*     */             //   #348	-> 122
/*     */             //   #349	-> 128
/*     */             //   #350	-> 139
/*     */             //   #351	-> 150
/*     */             //   #355	-> 159
/*     */             //   #356	-> 162
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success;
/*     */             //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error;
/*     */             //   22	137	1	index	I
/*     */             //   35	124	2	value	Lnet/minecraft/nbt/Tag;
/*     */             //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1;
/*     */             // Local variable type table:
/*     */             //   start	length	slot	name	signature
/*     */             //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */             //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */             //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1; }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class null
/*     */     extends AbstractIterator<T>
/*     */   {
/*     */     protected T computeNext() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield val$iterator : Ljava/util/ListIterator;
/*     */       //   4: invokeinterface hasNext : ()Z
/*     */       //   9: ifeq -> 162
/*     */       //   12: aload_0
/*     */       //   13: getfield val$iterator : Ljava/util/ListIterator;
/*     */       //   16: invokeinterface nextIndex : ()I
/*     */       //   21: istore_1
/*     */       //   22: aload_0
/*     */       //   23: getfield val$iterator : Ljava/util/ListIterator;
/*     */       //   26: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   31: checkcast net/minecraft/nbt/Tag
/*     */       //   34: astore_2
/*     */       //   35: aload_0
/*     */       //   36: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */       //   39: getfield codec : Lcom/mojang/serialization/Codec;
/*     */       //   42: aload_0
/*     */       //   43: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */       //   46: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */       //   49: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */       //   52: aload_2
/*     */       //   53: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */       //   58: dup
/*     */       //   59: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   62: pop
/*     */       //   63: astore_3
/*     */       //   64: iconst_0
/*     */       //   65: istore #4
/*     */       //   67: aload_3
/*     */       //   68: iload #4
/*     */       //   70: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   75: lookupswitch default -> 100, 0 -> 110, 1 -> 122
/*     */       //   100: new java/lang/MatchException
/*     */       //   103: dup
/*     */       //   104: aconst_null
/*     */       //   105: aconst_null
/*     */       //   106: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   109: athrow
/*     */       //   110: aload_3
/*     */       //   111: checkcast com/mojang/serialization/DataResult$Success
/*     */       //   114: astore #5
/*     */       //   116: aload #5
/*     */       //   118: invokevirtual value : ()Ljava/lang/Object;
/*     */       //   121: areturn
/*     */       //   122: aload_3
/*     */       //   123: checkcast com/mojang/serialization/DataResult$Error
/*     */       //   126: astore #6
/*     */       //   128: aload_0
/*     */       //   129: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */       //   132: iload_1
/*     */       //   133: aload_2
/*     */       //   134: aload #6
/*     */       //   136: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */       //   139: aload #6
/*     */       //   141: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */       //   144: invokevirtual isPresent : ()Z
/*     */       //   147: ifeq -> 159
/*     */       //   150: aload #6
/*     */       //   152: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */       //   155: invokevirtual get : ()Ljava/lang/Object;
/*     */       //   158: areturn
/*     */       //   159: goto -> 0
/*     */       //   162: aload_0
/*     */       //   163: invokevirtual endOfData : ()Ljava/lang/Object;
/*     */       //   166: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       //   #342	-> 12
/*     */       //   #343	-> 22
/*     */       //   #344	-> 35
/*     */       //   #345	-> 110
/*     */       //   #346	-> 116
/*     */       //   #348	-> 122
/*     */       //   #349	-> 128
/*     */       //   #350	-> 139
/*     */       //   #351	-> 150
/*     */       //   #355	-> 159
/*     */       //   #356	-> 162
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success;
/*     */       //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error;
/*     */       //   22	137	1	index	I
/*     */       //   35	124	2	value	Lnet/minecraft/nbt/Tag;
/*     */       //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */       //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */       //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CompoundListWrapper
/*     */     implements ValueInput.ValueInputList
/*     */   {
/*     */     private final ProblemReporter problemReporter;
/*     */ 
/*     */     
/*     */     private final ValueInputContextHelper context;
/*     */ 
/*     */     
/*     */     private final List<CompoundTag> list;
/*     */ 
/*     */     
/*     */     public CompoundListWrapper(ProblemReporter problemReporter, ValueInputContextHelper context, List<CompoundTag> list) {
/* 368 */       this.problemReporter = problemReporter;
/* 369 */       this.context = context;
/* 370 */       this.list = list;
/*     */     }
/*     */ 
/*     */     
/* 374 */     private ValueInput wrapChild(int index, CompoundTag compoundTag) { return TagValueInput.wrapChild(this.problemReporter.forChild(new ProblemReporter.IndexedPathElement(index)), this.context, compoundTag); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     public boolean isEmpty() { return this.list.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 384 */     public Stream<ValueInput> stream() { return Streams.mapWithIndex(this.list.stream(), (value, index) -> wrapChild((int)index, value)); }
/*     */ 
/*     */     
/*     */     public Iterator<ValueInput> iterator()
/*     */     {
/* 389 */       final ListIterator<CompoundTag> iterator = this.list.listIterator();
/* 390 */       return new AbstractIterator<ValueInput>()
/*     */         {
/*     */           protected ValueInput computeNext() {
/* 393 */             if (iterator.hasNext()) {
/* 394 */               int index = iterator.nextIndex();
/* 395 */               CompoundTag value = (CompoundTag)iterator.next();
/* 396 */               return TagValueInput.CompoundListWrapper.this.wrapChild(index, value);
/*     */             } 
/* 398 */             return (ValueInput)endOfData(); } }; } } class null extends AbstractIterator<ValueInput> { protected ValueInput computeNext() { if (iterator.hasNext()) { int index = iterator.nextIndex(); CompoundTag value = (CompoundTag)iterator.next(); return TagValueInput.CompoundListWrapper.this.wrapChild(index, value); }  return (ValueInput)endOfData(); }
/*     */      }
/*     */   public static final class DecodeFromFieldFailedProblem extends Record implements ProblemReporter.Problem { private final String name;
/*     */     private final Tag tag;
/*     */     private final DataResult.Error<?> error;
/*     */     
/* 404 */     public DecodeFromFieldFailedProblem(String name, Tag tag, DataResult.Error<?> error) { this.name = name; this.tag = tag; this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #404	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #404	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #404	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromFieldFailedProblem;
/* 404 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public Tag tag() { return this.tag; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 407 */     public String description() { return "Failed to decode value '" + String.valueOf(this.tag) + "' from field '" + this.name + "': " + this.error.message(); } }
/*     */   public static final class DecodeFromListFailedProblem extends Record implements ProblemReporter.Problem { private final String name; private final int index; private final Tag tag;
/*     */     private final DataResult.Error<?> error;
/*     */     
/* 411 */     public DecodeFromListFailedProblem(String name, int index, Tag tag, DataResult.Error<?> error) { this.name = name; this.index = index; this.tag = tag; this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;
/* 411 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public int index() { return this.index; } public Tag tag() { return this.tag; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 414 */     public String description() { return "Failed to decode value '" + String.valueOf(this.tag) + "' from field '" + this.name + "' at index " + this.index + "': " + this.error.message(); } }
/*     */   
/*     */   public static final class DecodeFromMapFailedProblem extends Record implements ProblemReporter.Problem { private final DataResult.Error<?> error;
/*     */     
/* 418 */     public DecodeFromMapFailedProblem(DataResult.Error<?> error) { this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #418	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #418	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #418	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;
/* 418 */       //   0	8	1	o	Ljava/lang/Object; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 421 */     public String description() { return "Failed to decode from map: " + this.error.message(); } }
/*     */   public static final class UnexpectedTypeProblem extends Record implements ProblemReporter.Problem { private final String name; private final TagType<?> expected;
/*     */     private final TagType<?> actual;
/*     */     
/* 425 */     public UnexpectedTypeProblem(String name, TagType<?> expected, TagType<?> actual) { this.name = name; this.expected = expected; this.actual = actual; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;
/* 425 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public TagType<?> expected() { return this.expected; } public TagType<?> actual() { return this.actual; }
/*     */ 
/*     */     
/* 428 */     public String description() { return "Expected field '" + this.name + "' to contain value of type " + this.expected.getName() + ", but got " + this.actual.getName(); } }
/*     */   public static final class UnexpectedNonNumberProblem extends Record implements ProblemReporter.Problem { private final String name;
/*     */     private final TagType<?> actual;
/*     */     
/* 432 */     public UnexpectedNonNumberProblem(String name, TagType<?> actual) { this.name = name; this.actual = actual; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;
/* 432 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public TagType<?> actual() { return this.actual; }
/*     */ 
/*     */     
/* 435 */     public String description() { return "Expected field '" + this.name + "' to contain number, but got " + this.actual.getName(); } }
/*     */   public static final class UnexpectedListElementTypeProblem extends Record implements ProblemReporter.Problem { private final String name; private final int index; private final TagType<?> expected;
/*     */     private final TagType<?> actual;
/*     */     
/* 439 */     public UnexpectedListElementTypeProblem(String name, int index, TagType<?> expected, TagType<?> actual) { this.name = name; this.index = index; this.expected = expected; this.actual = actual; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #439	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #439	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #439	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;
/* 439 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public int index() { return this.index; } public TagType<?> expected() { return this.expected; } public TagType<?> actual() { return this.actual; }
/*     */ 
/*     */     
/* 442 */     public String description() { return "Expected list '" + this.name + "' to contain at index " + this.index + " value of type " + this.expected.getName() + ", but got " + this.actual.getName(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\TagValueInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */