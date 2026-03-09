/*    */ package net.minecraft.network.chat.contents.data;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public final class BlockDataSource extends Record implements DataSource {
/*    */   private final String posPattern;
/*    */   
/* 19 */   public BlockDataSource(String posPattern, Coordinates compiledPos) { this.posPattern = posPattern; this.compiledPos = compiledPos; } private final Coordinates compiledPos; public String posPattern() { return this.posPattern; } public Coordinates compiledPos() { return this.compiledPos; }
/* 20 */   public static final MapCodec<BlockDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 21 */         .fieldOf("block").forGetter(BlockDataSource::posPattern))
/* 22 */       .apply(i, BlockDataSource::new));
/*    */ 
/*    */   
/* 25 */   public BlockDataSource(String pos) { this(pos, compilePos(pos)); }
/*    */ 
/*    */   
/*    */   private static Coordinates compilePos(String pos) {
/*    */     try {
/* 30 */       return BlockPosArgument.blockPos().parse(new StringReader(pos));
/* 31 */     } catch (CommandSyntaxException e) {
/* 32 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<CompoundTag> getData(CommandSourceStack sender) {
/* 38 */     if (this.compiledPos != null) {
/* 39 */       ServerLevel level = sender.getLevel();
/* 40 */       BlockPos pos = this.compiledPos.getBlockPos(sender);
/* 41 */       if (level.isLoaded(pos)) {
/* 42 */         BlockEntity entity = level.getBlockEntity(pos);
/*    */         
/* 44 */         if (entity != null) {
/* 45 */           return Stream.of(entity.saveWithFullMetadata(sender.registryAccess()));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     return Stream.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public MapCodec<BlockDataSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public String toString() { return "block=" + this.posPattern; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: if_acmpne -> 7
/*    */     //   5: iconst_1
/*    */     //   6: ireturn
/*    */     //   7: aload_1
/*    */     //   8: instanceof net/minecraft/network/chat/contents/data/BlockDataSource
/*    */     //   11: ifeq -> 37
/*    */     //   14: aload_1
/*    */     //   15: checkcast net/minecraft/network/chat/contents/data/BlockDataSource
/*    */     //   18: astore_2
/*    */     //   19: aload_0
/*    */     //   20: getfield posPattern : Ljava/lang/String;
/*    */     //   23: aload_2
/*    */     //   24: getfield posPattern : Ljava/lang/String;
/*    */     //   27: invokevirtual equals : (Ljava/lang/Object;)Z
/*    */     //   30: ifeq -> 37
/*    */     //   33: iconst_1
/*    */     //   34: goto -> 38
/*    */     //   37: iconst_0
/*    */     //   38: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     //   #66	-> 5
/*    */     //   #69	-> 7
/*    */     //   #68	-> 14
/*    */     //   #69	-> 27
/*    */     //   #68	-> 38
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   19	18	2	that	Lnet/minecraft/network/chat/contents/data/BlockDataSource;
/*    */     //   0	39	0	this	Lnet/minecraft/network/chat/contents/data/BlockDataSource;
/*    */     //   0	39	1	o	Ljava/lang/Object; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public int hashCode() { return this.posPattern.hashCode(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\data\BlockDataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */