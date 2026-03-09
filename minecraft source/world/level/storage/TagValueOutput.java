/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public class TagValueOutput
/*     */   implements ValueOutput {
/*     */   private final ProblemReporter problemReporter;
/*     */   private final DynamicOps<Tag> ops;
/*     */   private final CompoundTag output;
/*     */   
/*     */   private TagValueOutput(ProblemReporter problemReporter, DynamicOps<Tag> ops, CompoundTag output) {
/*  21 */     this.problemReporter = problemReporter;
/*  22 */     this.ops = ops;
/*  23 */     this.output = output;
/*     */   }
/*     */ 
/*     */   
/*  27 */   public static TagValueOutput createWithContext(ProblemReporter problemReporter, HolderLookup.Provider provider) { return new TagValueOutput(problemReporter, provider.createSerializationContext(NbtOps.INSTANCE), new CompoundTag()); }
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static TagValueOutput createWithoutContext(ProblemReporter problemReporter) { return new TagValueOutput(problemReporter, NbtOps.INSTANCE, new CompoundTag()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void store(String name, Codec<T> codec, T value) { // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_0
/*     */     //   2: getfield ops : Lcom/mojang/serialization/DynamicOps;
/*     */     //   5: aload_3
/*     */     //   6: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   11: dup
/*     */     //   12: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: pop
/*     */     //   16: astore #4
/*     */     //   18: iconst_0
/*     */     //   19: istore #5
/*     */     //   21: aload #4
/*     */     //   23: iload #5
/*     */     //   25: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   30: lookupswitch default -> 56, 0 -> 66, 1 -> 93
/*     */     //   56: new java/lang/MatchException
/*     */     //   59: dup
/*     */     //   60: aconst_null
/*     */     //   61: aconst_null
/*     */     //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   65: athrow
/*     */     //   66: aload #4
/*     */     //   68: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   71: astore #6
/*     */     //   73: aload_0
/*     */     //   74: getfield output : Lnet/minecraft/nbt/CompoundTag;
/*     */     //   77: aload_1
/*     */     //   78: aload #6
/*     */     //   80: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   83: checkcast net/minecraft/nbt/Tag
/*     */     //   86: invokevirtual put : (Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;
/*     */     //   89: pop
/*     */     //   90: goto -> 135
/*     */     //   93: aload #4
/*     */     //   95: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   98: astore #7
/*     */     //   100: aload_0
/*     */     //   101: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */     //   104: new net/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem
/*     */     //   107: dup
/*     */     //   108: aload_1
/*     */     //   109: aload_3
/*     */     //   110: aload #7
/*     */     //   112: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Object;Lcom/mojang/serialization/DataResult$Error;)V
/*     */     //   115: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */     //   120: aload #7
/*     */     //   122: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */     //   125: aload_0
/*     */     //   126: aload_1
/*     */     //   127: <illegal opcode> accept : (Lnet/minecraft/world/level/storage/TagValueOutput;Ljava/lang/String;)Ljava/util/function/Consumer;
/*     */     //   132: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */     //   135: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     //   #37	-> 66
/*     */     //   #38	-> 93
/*     */     //   #39	-> 100
/*     */     //   #40	-> 120
/*     */     //   #43	-> 135
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   73	20	6	success	Lcom/mojang/serialization/DataResult$Success;
/*     */     //   100	35	7	error	Lcom/mojang/serialization/DataResult$Error;
/*     */     //   0	136	0	this	Lnet/minecraft/world/level/storage/TagValueOutput;
/*     */     //   0	136	1	name	Ljava/lang/String;
/*     */     //   0	136	2	codec	Lcom/mojang/serialization/Codec;
/*     */     //   0	136	3	value	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   73	20	6	success	Lcom/mojang/serialization/DataResult$Success<Lnet/minecraft/nbt/Tag;>;
/*     */     //   100	35	7	error	Lcom/mojang/serialization/DataResult$Error<Lnet/minecraft/nbt/Tag;>;
/*     */     //   0	136	2	codec	Lcom/mojang/serialization/Codec<TT;>;
/*     */     //   0	136	3	value	TT; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void storeNullable(String name, Codec<T> codec, T value) {
/*  47 */     if (value != null) {
/*  48 */       store(name, codec, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void store(MapCodec<T> codec, T value) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokevirtual encoder : ()Lcom/mojang/serialization/Encoder;
/*     */     //   4: aload_0
/*     */     //   5: getfield ops : Lcom/mojang/serialization/DynamicOps;
/*     */     //   8: aload_2
/*     */     //   9: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   14: dup
/*     */     //   15: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   18: pop
/*     */     //   19: astore_3
/*     */     //   20: iconst_0
/*     */     //   21: istore #4
/*     */     //   23: aload_3
/*     */     //   24: iload #4
/*     */     //   26: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   31: lookupswitch default -> 56, 0 -> 66, 1 -> 91
/*     */     //   56: new java/lang/MatchException
/*     */     //   59: dup
/*     */     //   60: aconst_null
/*     */     //   61: aconst_null
/*     */     //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   65: athrow
/*     */     //   66: aload_3
/*     */     //   67: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   70: astore #5
/*     */     //   72: aload_0
/*     */     //   73: getfield output : Lnet/minecraft/nbt/CompoundTag;
/*     */     //   76: aload #5
/*     */     //   78: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   81: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   84: invokevirtual merge : (Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;
/*     */     //   87: pop
/*     */     //   88: goto -> 130
/*     */     //   91: aload_3
/*     */     //   92: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   95: astore #6
/*     */     //   97: aload_0
/*     */     //   98: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */     //   101: new net/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem
/*     */     //   104: dup
/*     */     //   105: aload_2
/*     */     //   106: aload #6
/*     */     //   108: invokespecial <init> : (Ljava/lang/Object;Lcom/mojang/serialization/DataResult$Error;)V
/*     */     //   111: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */     //   116: aload #6
/*     */     //   118: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */     //   121: aload_0
/*     */     //   122: <illegal opcode> accept : (Lnet/minecraft/world/level/storage/TagValueOutput;)Ljava/util/function/Consumer;
/*     */     //   127: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */     //   130: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     //   #55	-> 66
/*     */     //   #56	-> 91
/*     */     //   #57	-> 97
/*     */     //   #58	-> 116
/*     */     //   #61	-> 130
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   72	19	5	success	Lcom/mojang/serialization/DataResult$Success;
/*     */     //   97	33	6	error	Lcom/mojang/serialization/DataResult$Error;
/*     */     //   0	131	0	this	Lnet/minecraft/world/level/storage/TagValueOutput;
/*     */     //   0	131	1	codec	Lcom/mojang/serialization/MapCodec;
/*     */     //   0	131	2	value	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   72	19	5	success	Lcom/mojang/serialization/DataResult$Success<Lnet/minecraft/nbt/Tag;>;
/*     */     //   97	33	6	error	Lcom/mojang/serialization/DataResult$Error<Lnet/minecraft/nbt/Tag;>;
/*     */     //   0	131	1	codec	Lcom/mojang/serialization/MapCodec<TT;>;
/*     */     //   0	131	2	value	TT; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public void putBoolean(String name, boolean value) { this.output.putBoolean(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public void putByte(String name, byte value) { this.output.putByte(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void putShort(String name, short value) { this.output.putShort(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public void putInt(String name, int value) { this.output.putInt(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void putLong(String name, long value) { this.output.putLong(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public void putFloat(String name, float value) { this.output.putFloat(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public void putDouble(String name, double value) { this.output.putDouble(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void putString(String name, String value) { this.output.putString(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public void putIntArray(String name, int[] value) { this.output.putIntArray(name, value); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   private ProblemReporter reporterForChild(String name) { return this.problemReporter.forChild(new ProblemReporter.FieldPathElement(name)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueOutput child(String name) {
/* 114 */     CompoundTag childTag = new CompoundTag();
/* 115 */     this.output.put(name, childTag);
/* 116 */     return new TagValueOutput(reporterForChild(name), this.ops, childTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueOutput.ValueOutputList childrenList(String name) {
/* 121 */     ListTag childList = new ListTag();
/* 122 */     this.output.put(name, childList);
/* 123 */     return new ListWrapper(name, this.problemReporter, this.ops, childList);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ValueOutput.TypedOutputList<T> list(String name, Codec<T> codec) {
/* 128 */     ListTag childList = new ListTag();
/* 129 */     this.output.put(name, childList);
/* 130 */     return new TypedListWrapper(this.problemReporter, name, this.ops, codec, childList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public void discard(String name) { this.output.remove(name); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public boolean isEmpty() { return this.output.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public CompoundTag buildResult() { return this.output; }
/*     */   
/*     */   private static class ListWrapper
/*     */     implements ValueOutput.ValueOutputList {
/*     */     private final String fieldName;
/*     */     private final ProblemReporter problemReporter;
/*     */     private final DynamicOps<Tag> ops;
/*     */     private final ListTag output;
/*     */     
/*     */     private ListWrapper(String fieldName, ProblemReporter problemReporter, DynamicOps<Tag> ops, ListTag output) {
/* 154 */       this.fieldName = fieldName;
/* 155 */       this.problemReporter = problemReporter;
/* 156 */       this.ops = ops;
/* 157 */       this.output = output;
/*     */     }
/*     */ 
/*     */     
/*     */     public ValueOutput addChild() {
/* 162 */       int newChildIndex = this.output.size();
/* 163 */       CompoundTag child = new CompoundTag();
/* 164 */       this.output.add(child);
/* 165 */       return new TagValueOutput(this.problemReporter.forChild(new ProblemReporter.IndexedFieldPathElement(this.fieldName, newChildIndex)), this.ops, child);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 170 */     public void discardLast() { this.output.removeLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public boolean isEmpty() { return this.output.isEmpty(); }
/*     */   }
/*     */   
/*     */   private static class TypedListWrapper<T>
/*     */     extends Object implements ValueOutput.TypedOutputList<T> {
/*     */     private final ProblemReporter problemReporter;
/*     */     private final String name;
/*     */     private final DynamicOps<Tag> ops;
/*     */     private final Codec<T> codec;
/*     */     private final ListTag output;
/*     */     
/*     */     private TypedListWrapper(ProblemReporter problemReporter, String name, DynamicOps<Tag> ops, Codec<T> codec, ListTag output) {
/* 187 */       this.problemReporter = problemReporter;
/* 188 */       this.name = name;
/* 189 */       this.ops = ops;
/* 190 */       this.codec = codec;
/* 191 */       this.output = output;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(T value) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield codec : Lcom/mojang/serialization/Codec;
/*     */       //   4: aload_0
/*     */       //   5: getfield ops : Lcom/mojang/serialization/DynamicOps;
/*     */       //   8: aload_1
/*     */       //   9: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */       //   14: dup
/*     */       //   15: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   18: pop
/*     */       //   19: astore_2
/*     */       //   20: iconst_0
/*     */       //   21: istore_3
/*     */       //   22: aload_2
/*     */       //   23: iload_3
/*     */       //   24: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   29: lookupswitch default -> 56, 0 -> 66, 1 -> 91
/*     */       //   56: new java/lang/MatchException
/*     */       //   59: dup
/*     */       //   60: aconst_null
/*     */       //   61: aconst_null
/*     */       //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   65: athrow
/*     */       //   66: aload_2
/*     */       //   67: checkcast com/mojang/serialization/DataResult$Success
/*     */       //   70: astore #4
/*     */       //   72: aload_0
/*     */       //   73: getfield output : Lnet/minecraft/nbt/ListTag;
/*     */       //   76: aload #4
/*     */       //   78: invokevirtual value : ()Ljava/lang/Object;
/*     */       //   81: checkcast net/minecraft/nbt/Tag
/*     */       //   84: invokevirtual add : (Ljava/lang/Object;)Z
/*     */       //   87: pop
/*     */       //   88: goto -> 142
/*     */       //   91: aload_2
/*     */       //   92: checkcast com/mojang/serialization/DataResult$Error
/*     */       //   95: astore #5
/*     */       //   97: aload_0
/*     */       //   98: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */       //   101: new net/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem
/*     */       //   104: dup
/*     */       //   105: aload_0
/*     */       //   106: getfield name : Ljava/lang/String;
/*     */       //   109: aload_1
/*     */       //   110: aload #5
/*     */       //   112: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Object;Lcom/mojang/serialization/DataResult$Error;)V
/*     */       //   115: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */       //   120: aload #5
/*     */       //   122: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */       //   125: aload_0
/*     */       //   126: getfield output : Lnet/minecraft/nbt/ListTag;
/*     */       //   129: dup
/*     */       //   130: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   133: pop
/*     */       //   134: <illegal opcode> accept : (Lnet/minecraft/nbt/ListTag;)Ljava/util/function/Consumer;
/*     */       //   139: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */       //   142: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       //   #197	-> 66
/*     */       //   #198	-> 91
/*     */       //   #199	-> 97
/*     */       //   #200	-> 120
/*     */       //   #203	-> 142
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   72	19	4	success	Lcom/mojang/serialization/DataResult$Success;
/*     */       //   97	45	5	error	Lcom/mojang/serialization/DataResult$Error;
/*     */       //   0	143	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$TypedListWrapper;
/*     */       //   0	143	1	value	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   72	19	4	success	Lcom/mojang/serialization/DataResult$Success<Lnet/minecraft/nbt/Tag;>;
/*     */       //   97	45	5	error	Lcom/mojang/serialization/DataResult$Error<Lnet/minecraft/nbt/Tag;>;
/*     */       //   0	143	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$TypedListWrapper<TT;>;
/*     */       //   0	143	1	value	TT; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     public boolean isEmpty() { return this.output.isEmpty(); } }
/*     */   public static final class EncodeToFieldFailedProblem extends Record implements ProblemReporter.Problem { private final String name; private final Object value;
/*     */     private final DataResult.Error<?> error;
/*     */     
/* 211 */     public EncodeToFieldFailedProblem(String name, Object value, DataResult.Error<?> error) { this.name = name; this.value = value; this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #211	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 211 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #211	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #211	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToFieldFailedProblem;
/* 211 */       //   0	8	1	o	Ljava/lang/Object; } public Object value() { return this.value; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 214 */     public String description() { return "Failed to encode value '" + String.valueOf(this.value) + "' to field '" + this.name + "': " + this.error.message(); } }
/*     */   public static final class EncodeToListFailedProblem extends Record implements ProblemReporter.Problem { private final String name; private final Object value;
/*     */     private final DataResult.Error<?> error;
/*     */     
/* 218 */     public EncodeToListFailedProblem(String name, Object value, DataResult.Error<?> error) { this.name = name; this.value = value; this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;
/* 218 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public Object value() { return this.value; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 221 */     public String description() { return "Failed to append value '" + String.valueOf(this.value) + "' to list '" + this.name + "': " + this.error.message(); } }
/*     */   public static final class EncodeToMapFailedProblem extends Record implements ProblemReporter.Problem { private final Object value;
/*     */     private final DataResult.Error<?> error;
/*     */     
/* 225 */     public EncodeToMapFailedProblem(Object value, DataResult.Error<?> error) { this.value = value; this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;
/* 225 */       //   0	8	1	o	Ljava/lang/Object; } public Object value() { return this.value; } public DataResult.Error<?> error() { return this.error; }
/*     */ 
/*     */     
/* 228 */     public String description() { return "Failed to merge value '" + String.valueOf(this.value) + "' to an object: " + this.error.message(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\TagValueOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */