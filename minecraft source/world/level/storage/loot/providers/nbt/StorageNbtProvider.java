/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class StorageNbtProvider extends Record implements NbtProvider {
/*    */   private final Identifier id;
/*    */   
/* 12 */   public StorageNbtProvider(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final MapCodec<StorageNbtProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 14 */         .fieldOf("source").forGetter(StorageNbtProvider::id))
/* 15 */       .apply(i, StorageNbtProvider::new));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public LootNbtProviderType getType() { return NbtProviders.STORAGE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public Tag get(LootContext context) { return context.getLevel().getServer().getCommandStorage().get(this.id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\nbt\StorageNbtProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */