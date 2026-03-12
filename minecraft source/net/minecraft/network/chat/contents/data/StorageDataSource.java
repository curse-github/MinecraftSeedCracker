/*    */ package net.minecraft.network.chat.contents.data;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class StorageDataSource extends Record implements DataSource {
/*    */   private final Identifier id;
/*    */   
/* 11 */   public StorageDataSource(Identifier id) { this.id = id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource; } public Identifier id() { return this.id; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final MapCodec<StorageDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 13 */         .fieldOf("storage").forGetter(StorageDataSource::id))
/* 14 */       .apply(i, StorageDataSource::new));
/*    */ 
/*    */   
/*    */   public Stream<CompoundTag> getData(CommandSourceStack sender) {
/* 18 */     CompoundTag tag = sender.getServer().getCommandStorage().get(this.id);
/* 19 */     return Stream.of(tag);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<StorageDataSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public String toString() { return "storage=" + String.valueOf(this.id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\data\StorageDataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */