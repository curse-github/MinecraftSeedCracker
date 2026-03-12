/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NumericTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class StorageValue extends Record implements NumberProvider {
/*    */   private final Identifier storage;
/*    */   private final NbtPathArgument.NbtPath path;
/*    */   
/* 15 */   public StorageValue(Identifier storage, NbtPathArgument.NbtPath path) { this.storage = storage; this.path = path; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue; } public Identifier storage() { return this.storage; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public NbtPathArgument.NbtPath path() { return this.path; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final MapCodec<StorageValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 20 */         .fieldOf("storage").forGetter(StorageValue::storage), NbtPathArgument.NbtPath.CODEC
/* 21 */         .fieldOf("path").forGetter(StorageValue::path))
/* 22 */       .apply(i, StorageValue::new));
/*    */ 
/*    */ 
/*    */   
/* 26 */   public LootNumberProviderType getType() { return NumberProviders.STORAGE; }
/*    */ 
/*    */   
/*    */   private Number getNumericTag(LootContext context, Number _default) {
/* 30 */     CompoundTag value = context.getLevel().getServer().getCommandStorage().get(this.storage);
/*    */     
/*    */     try {
/* 33 */       List<Tag> selectedTags = this.path.get(value);
/* 34 */       if (selectedTags.size() == 1) {
/* 35 */         Object object = selectedTags.getFirst(); if (object instanceof NumericTag) { NumericTag result = (NumericTag)object;
/* 36 */           return result.box(); }
/*    */       
/*    */       } 
/* 39 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*    */ 
/*    */     
/* 42 */     return _default;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public float getFloat(LootContext context) { return getNumericTag(context, Float.valueOf(0.0F)).floatValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getInt(LootContext context) { return getNumericTag(context, Integer.valueOf(0)).intValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\StorageValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */