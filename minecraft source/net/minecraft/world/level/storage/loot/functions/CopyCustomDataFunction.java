/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class CopyCustomDataFunction extends LootItemConditionalFunction {
/*     */   private static final class CopyOperation extends Record {
/*     */     private final NbtPathArgument.NbtPath sourcePath;
/*     */     private final NbtPathArgument.NbtPath targetPath;
/*     */     private final CopyCustomDataFunction.MergeStrategy op;
/*     */     
/*  31 */     private CopyOperation(NbtPathArgument.NbtPath sourcePath, NbtPathArgument.NbtPath targetPath, CopyCustomDataFunction.MergeStrategy op) { this.sourcePath = sourcePath; this.targetPath = targetPath; this.op = op; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  31 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; } public NbtPathArgument.NbtPath sourcePath() { return this.sourcePath; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*  31 */       //   0	8	1	o	Ljava/lang/Object; } public NbtPathArgument.NbtPath targetPath() { return this.targetPath; } public CopyCustomDataFunction.MergeStrategy op() { return this.op; }
/*  32 */     public static final Codec<CopyOperation> CODEC = RecordCodecBuilder.create(i -> i.group(NbtPathArgument.NbtPath.CODEC
/*  33 */           .fieldOf("source").forGetter(CopyOperation::sourcePath), NbtPathArgument.NbtPath.CODEC
/*  34 */           .fieldOf("target").forGetter(CopyOperation::targetPath), CopyCustomDataFunction.MergeStrategy.CODEC
/*  35 */           .fieldOf("op").forGetter(CopyOperation::op))
/*  36 */         .apply(i, CopyOperation::new));
/*     */     
/*     */     public void apply(Supplier<Tag> target, Tag source) {
/*     */       try {
/*  40 */         List<Tag> sourceTags = this.sourcePath.get(source);
/*  41 */         if (!sourceTags.isEmpty()) {
/*  42 */           this.op.merge((Tag)target.get(), this.targetPath, sourceTags);
/*     */         }
/*  44 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final MapCodec<CopyCustomDataFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(NbtProviders.CODEC
/*  51 */           .fieldOf("source").forGetter(()), CopyOperation.CODEC
/*  52 */           .listOf().fieldOf("ops").forGetter(())))
/*  53 */       .apply(i, CopyCustomDataFunction::new));
/*     */   
/*     */   private final NbtProvider source;
/*     */   private final List<CopyOperation> operations;
/*     */   
/*     */   private CopyCustomDataFunction(List<LootItemCondition> predicates, NbtProvider source, List<CopyOperation> operations) {
/*  59 */     super(predicates);
/*  60 */     this.source = source;
/*  61 */     this.operations = List.copyOf(operations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public LootItemFunctionType<CopyCustomDataFunction> getType() { return LootItemFunctions.COPY_CUSTOM_DATA; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.source.getReferencedContextParams(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  76 */     Tag sourceTag = this.source.get(context);
/*  77 */     if (sourceTag == null) {
/*  78 */       return itemStack;
/*     */     }
/*     */     
/*  81 */     MutableObject<CompoundTag> result = new MutableObject<CompoundTag>();
/*  82 */     Supplier<Tag> lazyTargetCopy = () -> {
/*  83 */         if (result.get() == null) {
/*  84 */           result.setValue(((CustomData)itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag());
/*     */         }
/*  86 */         return (Tag)result.get();
/*     */       };
/*  88 */     this.operations.forEach(op -> op.apply(lazyTargetCopy, sourceTag));
/*  89 */     CompoundTag resultTag = (CompoundTag)result.get();
/*  90 */     if (resultTag != null) {
/*  91 */       CustomData.set(DataComponents.CUSTOM_DATA, itemStack, resultTag);
/*     */     }
/*     */     
/*  94 */     return itemStack;
/*     */   }
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final NbtProvider source; private final List<CopyCustomDataFunction.CopyOperation> ops;
/*     */     
/*     */     private Builder(NbtProvider source) {
/*  99 */       this.ops = Lists.newArrayList();
/*     */ 
/*     */       
/* 102 */       this.source = source;
/*     */     }
/*     */     
/*     */     public Builder copy(String sourcePath, String targetPath, CopyCustomDataFunction.MergeStrategy mergeStrategy) {
/*     */       try {
/* 107 */         this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(sourcePath), NbtPathArgument.NbtPath.of(targetPath), mergeStrategy));
/* 108 */       } catch (CommandSyntaxException e) {
/* 109 */         throw new IllegalArgumentException(e);
/*     */       } 
/* 111 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 115 */     public Builder copy(String sourcePath, String targetPath) { return copy(sourcePath, targetPath, CopyCustomDataFunction.MergeStrategy.REPLACE); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     protected Builder getThis() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     public LootItemFunction build() { return new CopyCustomDataFunction(getConditions(), this.source, this.ops); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 134 */   public static Builder copyData(NbtProvider source) { return new Builder(source); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static Builder copyData(LootContext.EntityTarget source) { return new Builder(ContextNbtProvider.forContextEntity(source)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final abstract enum MergeStrategy
/*     */     implements StringRepresentable
/*     */   {
/*     */     REPLACE, APPEND, MERGE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final Codec<MergeStrategy> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$1
/*     */       //   3: dup
/*     */       //   4: ldc 'REPLACE'
/*     */       //   6: iconst_0
/*     */       //   7: ldc 'replace'
/*     */       //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   12: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.REPLACE : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */       //   15: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$2
/*     */       //   18: dup
/*     */       //   19: ldc 'APPEND'
/*     */       //   21: iconst_1
/*     */       //   22: ldc 'append'
/*     */       //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   27: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.APPEND : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */       //   30: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$3
/*     */       //   33: dup
/*     */       //   34: ldc 'MERGE'
/*     */       //   36: iconst_2
/*     */       //   37: ldc 'merge'
/*     */       //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   42: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.MERGE : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */       //   45: invokestatic $values : ()[Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */       //   48: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.$VALUES : [Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */       //   51: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */       //   56: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   59: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.CODEC : Lcom/mojang/serialization/Codec;
/*     */       //   62: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       //   #148	-> 15
/*     */       //   #160	-> 30
/*     */       //   #141	-> 45
/*     */       //   #177	-> 51
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     MergeStrategy(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     public String getSerializedName() { return this.name; }
/*     */     
/*     */     public abstract void merge(Tag param1Tag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException { path.set(target, (Tag)Iterables.getLast(sources)); }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, ListTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof ListTag)
/*     */               sources.forEach(()); 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof CompoundTag)
/*     */               sources.forEach(()); 
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */