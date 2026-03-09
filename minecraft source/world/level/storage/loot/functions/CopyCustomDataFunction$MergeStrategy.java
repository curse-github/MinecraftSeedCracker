/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ public static final abstract enum MergeStrategy
/*     */   implements StringRepresentable
/*     */ {
/*     */   REPLACE, APPEND, MERGE;
/*     */   public static final Codec<MergeStrategy> CODEC;
/*     */   private final String name;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$1
/*     */     //   3: dup
/*     */     //   4: ldc 'REPLACE'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'replace'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.REPLACE : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */     //   15: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$2
/*     */     //   18: dup
/*     */     //   19: ldc 'APPEND'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'append'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.APPEND : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */     //   30: new net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy$3
/*     */     //   33: dup
/*     */     //   34: ldc 'MERGE'
/*     */     //   36: iconst_2
/*     */     //   37: ldc 'merge'
/*     */     //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   42: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.MERGE : Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */     //   45: invokestatic $values : ()[Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */     //   48: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.$VALUES : [Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy;
/*     */     //   51: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */     //   56: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   59: putstatic net/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$MergeStrategy.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     //   #148	-> 15
/*     */     //   #160	-> 30
/*     */     //   #141	-> 45
/*     */     //   #177	-> 51
/*     */   }
/*     */   
/* 184 */   MergeStrategy(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public String getSerializedName() { return this.name; }
/*     */   
/*     */   public abstract void merge(Tag paramTag, NbtPathArgument.NbtPath paramNbtPath, List<Tag> paramList) throws CommandSyntaxException;
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$MergeStrategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */