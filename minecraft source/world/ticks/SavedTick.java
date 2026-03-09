/*    */ package net.minecraft.world.ticks;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class SavedTick<T> extends Record {
/*    */   private final T type;
/*    */   private final BlockPos pos;
/*    */   private final int delay;
/*    */   private final TickPriority priority;
/*    */   
/* 14 */   public SavedTick(T type, BlockPos pos, int delay, TickPriority priority) { this.type = type; this.pos = pos; this.delay = delay; this.priority = priority; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ticks/SavedTick;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public T type() { return (T)this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ticks/SavedTick;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ticks/SavedTick;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>; } public BlockPos pos() { return this.pos; } public int delay() { return this.delay; } public TickPriority priority() { return this.priority; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Codec<SavedTick<T>> codec(Codec<T> typeCodec) {
/* 21 */     MapCodec<BlockPos> posCodec = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 22 */           .fieldOf("x").forGetter(Vec3i::getX), Codec.INT
/* 23 */           .fieldOf("y").forGetter(Vec3i::getY), Codec.INT
/* 24 */           .fieldOf("z").forGetter(Vec3i::getZ))
/* 25 */         .apply(i, BlockPos::new));
/* 26 */     return RecordCodecBuilder.create(i -> i.group(typeCodec
/* 27 */           .fieldOf("i").forGetter(SavedTick::type), posCodec
/* 28 */           .forGetter(SavedTick::pos), Codec.INT
/* 29 */           .fieldOf("t").forGetter(SavedTick::delay), TickPriority.CODEC
/* 30 */           .fieldOf("p").forGetter(SavedTick::priority))
/* 31 */         .apply(i, SavedTick::new));
/*    */   }
/*    */   
/* 34 */   public static final Hash.Strategy<SavedTick<?>> UNIQUE_TICK_HASH = new Hash.Strategy<SavedTick<?>>()
/*    */     {
/*    */       public int hashCode(SavedTick<?> o) {
/* 37 */         return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean equals(SavedTick<?> a, SavedTick<?> b) {
/* 42 */         if (a == b) {
/* 43 */           return true;
/*    */         }
/* 45 */         if (a == null || b == null) {
/* 46 */           return false;
/*    */         }
/* 48 */         return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */       }
/*    */     };
/*    */   
/*    */   public static <T> List<SavedTick<T>> filterTickListForChunk(List<SavedTick<T>> savedTicks, ChunkPos chunkPos) {
/* 53 */     long posKey = chunkPos.toLong();
/* 54 */     return savedTicks.stream()
/* 55 */       .filter(tick -> 
/*    */ 
/*    */         
/* 58 */         (ChunkPos.asLong(tick.pos()) == posKey))
/*    */       
/* 60 */       .toList();
/*    */   }
/*    */ 
/*    */   
/* 64 */   public ScheduledTick<T> unpack(long currentTick, long currentSubTick) { return new ScheduledTick(this.type, this.pos, currentTick + this.delay, this.priority, currentSubTick); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static <T> SavedTick<T> probe(T type, BlockPos pos) { return new SavedTick(type, pos, 0, TickPriority.NORMAL); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\SavedTick.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */