/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CopyOperation
/*    */   extends Record
/*    */ {
/*    */   private final NbtPathArgument.NbtPath sourcePath;
/*    */   private final NbtPathArgument.NbtPath targetPath;
/*    */   private final CopyCustomDataFunction.MergeStrategy op;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 31 */   private CopyOperation(NbtPathArgument.NbtPath sourcePath, NbtPathArgument.NbtPath targetPath, CopyCustomDataFunction.MergeStrategy op) { this.sourcePath = sourcePath; this.targetPath = targetPath; this.op = op; } public NbtPathArgument.NbtPath sourcePath() { return this.sourcePath; } public NbtPathArgument.NbtPath targetPath() { return this.targetPath; } public CopyCustomDataFunction.MergeStrategy op() { return this.op; }
/* 32 */   public static final Codec<CopyOperation> CODEC = RecordCodecBuilder.create(i -> i.group(NbtPathArgument.NbtPath.CODEC
/* 33 */         .fieldOf("source").forGetter(CopyOperation::sourcePath), NbtPathArgument.NbtPath.CODEC
/* 34 */         .fieldOf("target").forGetter(CopyOperation::targetPath), CopyCustomDataFunction.MergeStrategy.CODEC
/* 35 */         .fieldOf("op").forGetter(CopyOperation::op))
/* 36 */       .apply(i, CopyOperation::new));
/*    */   
/*    */   public void apply(Supplier<Tag> target, Tag source) {
/*    */     try {
/* 40 */       List<Tag> sourceTags = this.sourcePath.get(source);
/* 41 */       if (!sourceTags.isEmpty()) {
/* 42 */         this.op.merge((Tag)target.get(), this.targetPath, sourceTags);
/*    */       }
/* 44 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$CopyOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */